package es.upm.oeg.pnk.actions

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by cbadenes on 04/08/15.
 */
object ModelEntitiesW2VBuilderExample {

  def main(args: Array[String]): Unit = {

    // Spark Configuration
    val conf = new SparkConf().
      setMaster("local[4]").
      setAppName("Local Spark Example").
      set("spark.executor.memory", "8g").
      set("spark.driver.maxResultSize", "4g")
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

    // Create an initial w2v model from raw text
    ModelBuilder.createEntitiesW2V(sc)

  }


}
