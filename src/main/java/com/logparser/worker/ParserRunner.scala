package com.logparser.worker


import com.logparser.util.TimeUtil
import org.apache.spark.{SparkConf, SparkContext}


object ParserRunner {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("totalParser")
    val sc = new SparkContext(conf)
    val dateStr = TimeUtil.getPre1HourTime
//    val dateStr = "2019-02-15-11"
    TopResultParser.parseRecMonitor(sc, dateStr)
    SecResultParser.parseRecMonitor(sc, dateStr)
    FeedbackParser.parseRecMonitor(sc, dateStr)
  }
}
