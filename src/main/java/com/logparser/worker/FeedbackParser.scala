package com.logparser.worker

import com.logparser.dao.FeedbackLogDao
import com.logparser.logmodel.FeedbackLogModel
import com.logparser.userdao.UserInfoDao
import com.logparser.util.{ConfigUtil, TimeUtil}
import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}

object FeedbackParser extends BaseParser {
  val logger = Logger.getLogger(this.getClass)

  def parseRecMonitor(sc: SparkContext, dateStr: String): Unit = {

    //2019-01-21 16:02:20,964 INFO [com.cmri.recengine.controller.FeedbackController] - trace_id=v1_1_1_1_null_1548054001205_025270776643817571 userid=13905183862 itemid=02|500023741 type=play operate_time=1548057740849

    if (!hdfsPathExits(sc, ConfigUtil.getFeedbackLogPath + dateStr + "/")) {
      logger.info("recMonitor data path not exists!");
      return
    }

    val feedbackLog = sc.textFile(ConfigUtil.getFeedbackLogPath + dateStr + "/*")
    //    val feedbackLog = sc.textFile("/Users/qianjay/feedback.log")


    val intoArray = feedbackLog.flatMap { line =>
      val words = line.split(" ")
      if (words.length == feedbackLogLen)
        Some(words)
      else None
    }

    // line(7) userid
    val userids = intoArray.flatMap { line =>
      val words = line(6).split("=")
      if (words.length == 2)
        Some(words(1))
      else None
    }.distinct().collect()

    val userWithCity = UserInfoDao.getUserCity(userids)

    sc.broadcast(userWithCity)

    val midRes = intoArray.flatMap { line =>
      if (!line(5).equalsIgnoreCase("catename=topitems")) {
        try {
          val trace = line(5).split("=")(1)
          val tracelist = trace.split("_")
          val cpid = tracelist(6).substring(0, 2)
          val userid = line(6).split("=")(1)
          val bucketnum = tracelist(1)
          var area = "unk"
          if (userWithCity.containsKey(userid))
            area = userWithCity.get(userid)

          // traceid userid itemid
          val list = Array[String](line(5), line(6), line(7))
          // (cpid area bucketnum, (type, (traceid ...)))
          Some(cpid + " " + area + " " + bucketnum, (line(8), list))
        } catch {
          case e: Exception => {
            logger.info("error parsing " + line.mkString(" "))
            None
          }
        }
      } else None
    }
    val displayCnt = midRes.flatMap { line =>
      if (line._2._1.equalsIgnoreCase("type=display"))
        Some(line._1, line._2._2(2).split(",").length.toLong)
      else None
    }.reduceByKey(_ + _)
      .foreach { line =>
        val words = line._1.split(" ")

        val cpid = words(0)
        val area = words(1)
        val bucketnum = words(2)

        val model = new FeedbackLogModel
        model.setTime(dateStr)
        model.setArea(area)
        model.setBucketnum(bucketnum)
        model.setCpid(cpid)
        model.setType("display")
        model.setCnt(line._2)
      }

    midRes.flatMap { line =>
      if (line._2._1.equalsIgnoreCase("type=play"))
        Some(line._1, 1l)
      else None
    }.reduceByKey(_ + _)
      .foreach { line =>
        val words = line._1.split(" ")

        val cpid = words(0)
        val area = words(1)
        val bucketnum = words(2)

        val model = new FeedbackLogModel
        model.setTime(dateStr)
        model.setArea(area)
        model.setBucketnum(bucketnum)
        model.setCpid(cpid)
        model.setType("play")
        model.setCnt(line._2)
        FeedbackLogDao.addNewEntry(model)
      }
    midRes.flatMap { line =>
      if (line._2._1.equalsIgnoreCase("type=click"))
        Some(line._1, 1l)
      else None
    }.reduceByKey(_ + _)
      .foreach { line =>
        val words = line._1.split(" ")

        val cpid = words(0)
        val area = words(1)
        val bucketnum = words(2)

        val model = new FeedbackLogModel
        model.setTime(dateStr)
        model.setArea(area)
        model.setBucketnum(bucketnum)
        model.setCpid(cpid)
        model.setType("click")
        model.setCnt(line._2)
        FeedbackLogDao.addNewEntry(model)
      }

    midRes.map {
      line => (line._1, line._2._2(0))
    }.map(line => (line._1, 1l)).reduceByKey(_ + _)
      .foreach { line =>
        val words = line._1.split(" ")

        val cpid = words(0)
        val area = words(1)
        val bucketnum = words(2)

        val model = new FeedbackLogModel
        model.setTime(dateStr)
        model.setArea(area)
        model.setBucketnum(bucketnum)
        model.setCpid(cpid)
        model.setType("recnum")
        model.setCnt(line._2)
        FeedbackLogDao.addNewEntry(model)
      }

  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[4]").setAppName("SKtest")
    val sc = new SparkContext(conf)
    FeedbackParser.parseRecMonitor(sc, TimeUtil.getPre1HourTime)
  }
}
