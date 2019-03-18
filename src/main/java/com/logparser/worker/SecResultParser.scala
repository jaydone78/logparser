package com.logparser.worker

import com.logparser.dao.{SecResultAlbumCntModelDao, SecResultLogModelDao, TopResultAlbumCntModelDao, TopResultLogModelDao}
import com.logparser.logmodel.{ResultAlbumCntModel, ResultLogModel, TopResultAlbumCntModel}
import com.logparser.userdao.UserInfoDao
import com.logparser.util.{ConfigUtil, TimeUtil}
import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object SecResultParser extends BaseParser {

  val LOG = Logger.getLogger(this.getClass)

  def parseRecMonitor(sc: SparkContext, dateStr: String): Unit = {

    //2019-02-11 00:01:03,133 DEBUG [resultlog] - catename=topitems trace_id=v1_2_1_1_2d85102e1a66008f2682d82cd6724184_1549814463133_01asdqwertyfghuioj userid=2d85102e1a66008f2682d82cd6724184 itemid=01|1140647 recall_string=1_,1_,1_,1_,1_,1_,1_,1_,1_,1_ recall_score=30_,28_,27_,26_,25_,24_,23_,22_,21_,17_

    if (!hdfsPathExits(sc, ConfigUtil.getResultLogPath + dateStr + "/")) {
      LOG.info("recMonitor data path not exists!");
      return
    }

    val resultLog = sc.textFile(ConfigUtil.getResultLogPath + dateStr + "/*")

    //    val resultLog = sc.textFile("/Users/qianjay/result.log")


    val intoArray = resultLog.flatMap { line =>
      val words = line.split(" ")
      if (words.length == RESULT_SEG_LEN)
        Some(words)
      else None
    }

    // line(7) userid
    val userids = intoArray.flatMap { line =>
      val words = line(7).split("=")
      if (words.length == 2)
        Some(words(1))
      else None
    }.distinct().collect()

    val userWithCity = UserInfoDao.getUserCity(userids)

    sc.broadcast(userWithCity)

    val midRes = intoArray.flatMap { line =>
      if (!line(5).equalsIgnoreCase("catename=topitems")) {
        try {
          val trace = line(6).split("=")(1)
          val tracelist = trace.split("_")
          val cpid = tracelist(6).substring(0, 2)
          val userid = line(7).split("=")(1)
          var area = "unk"
          if (userWithCity.containsKey(userid))
            area = userWithCity.get(userid)

          // traceid userid itemid
          val list = Array[String](line(6), line(7), line(8))
          // cpid area
          Some(cpid + " " + area, list)
        }
      } else None
    }

    val uv = midRes.map { line =>
      (line._1, line._2(1))
    }.distinct()
      .map(line => (line._1, 1l))
      .reduceByKey(_ + _)

    val pv = midRes.map { line =>
      (line._1, (line._2(1), line._2(2).split(",").length.toLong))
    }.distinct()
      .map(line => (line._1, line._2._2))
      .reduceByKey(_ + _)


    val albums = midRes.map { line =>
      (line._1, line._2(2))
    }.flatMap { line =>
      val ids = line._2.split("=")(1).split(",")
      for (i <- 0 until ids.length) yield {
        (line._1, ids(i))
      }
    }

    // (bigKey, albumid)  => (bigKey, cnt)
    val albumCnt = albums.distinct()
      .map(line => (line._1, 1l))
      .reduceByKey(_ + _)

    val albumsWithId = albums
      .map(line => ((line._1, line._2), 1l))
      .reduceByKey(_ + _)
      .map(line => (line._1._1, ArrayBuffer((line._2, line._1._2))))
      .reduceByKey((u, v) => (u ++ v))
      .flatMap { line =>
        val tmp = line._2.sorted.reverse.take(TOP_SIZE)
        for (i <- 0 until tmp.length) yield {
          ((line._1, tmp(i)._2), tmp(i)._1)
        }
      }

    uv.join(pv).join(albumCnt).foreach { line =>
      val words = line._1.split(" ")
      val cpid = words(0)
      val area = words(1)
      val model = new ResultLogModel
      model.setTime(dateStr)
      model.setCpid(cpid)
      model.setCity(area)
      model.setUv(line._2._1._1)
      model.setPv(line._2._1._2)
      model.setAlbumnum(line._2._2)
      SecResultLogModelDao.addNewEntry(model)
    }

    albumsWithId.foreach { line =>
      val words = line._1._1.split(" ")
      val albumid = line._1._2
      val albumcnt = line._2
      val model = new ResultAlbumCntModel
      val cpid = words(0)
      val area = words(1)
      model.setTime(dateStr)
      model.setCpid(cpid)
      model.setCity(area)
      model.setAlbumid(albumid)
      model.setAlbumcnt(albumcnt)
      SecResultAlbumCntModelDao.addNewEntry(model)
    }
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[4]").setAppName("SKtest")
    val sc = new SparkContext(conf)
    SecResultParser.parseRecMonitor(sc, TimeUtil.getPre1HourTime)
  }
}