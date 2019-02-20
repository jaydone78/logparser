package com.logparser.worker

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkContext

trait BaseParser {
  val resultSegLen = 11
  val feedbackLogLen = 10

  def hdfsPathExits(sc: SparkContext, path: String): Boolean = {
    val conf = sc.hadoopConfiguration
    val fs = FileSystem.get(conf)
    return fs.exists(new Path(path))
  }
}
