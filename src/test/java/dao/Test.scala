package dao

import org.apache.spark.{SparkConf, SparkContext}

object Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[4]").setAppName("SKtest")
    val sc = new SparkContext(conf)
    val midRes = sc.parallelize(Array[Long](2, 3, 4, 5, 6, 7))
//    midRes.map(l=>(1, l))
//      .groupByKey()
//      .flatMap { line =>
//        val top200 = line._2.toList.sorted.reverse.take(4)
//        top200.map(l => (line._1, l))
//      }.foreach(println)
    midRes.map(l=>(1, l))
      .sortBy(_._2, false)
      .take(2)
      .foreach(println)
  }
}
