package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.Analyze
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

    ModelBuilder.createW2V(sc,Analyze.corpus_tagged,Analyze.w2v_ent)

  }


}
