package com.logparser.worker

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkContext

trait BaseParser {
  val RESULT_SEG_LEN = 11
  val FEEDBACK_LOG_LEN = 10
  val TOP_SIZE = 200

  def hdfsPathExits(sc: SparkContext, path: String): Boolean = {
    val conf = sc.hadoopConfiguration
    val fs = FileSystem.get(conf)
    return fs.exists(new Path(path))
  }
}
