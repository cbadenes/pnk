package es.upm.oeg.pnk

import es.upm.oeg.pnk.data.GraphBuilder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.feature.Word2VecModel
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by cbadenes on 05/08/15.
 */
object GraphPersonas {

  def main(args: Array[String]): Unit = {

    // Spark Configuration
    val conf = new SparkConf().
      setMaster("local[4]").
      setAppName("Local Spark Example").
      set("spark.executor.memory", "48g").
      set("spark.driver.maxResultSize","8g")
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

    println(s"loading word2vec model to detect similar words..")
    val w2vModel : Word2VecModel    = Word2VecModel.load(sc, Analyze.w2v_optimized)

    val input: RDD[(String, Long)] = sc.textFile("entities/personas.txt").zipWithIndex.cache

    val json = GraphBuilder(w2vModel,input)

    println(json.toString)



  }

}
