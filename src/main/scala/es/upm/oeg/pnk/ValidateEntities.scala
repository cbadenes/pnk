package es.upm.oeg.pnk

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.feature.Word2VecModel
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by cbadenes on 05/08/15.
 */
object ValidateEntities {

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
    val w2vModel    = Word2VecModel.load(sc, Analyze.w2v_optimized)

    sc.wholeTextFiles("output/entities/raw-LUG").flatMap(x=>x._2.split("\n")).foreach{case company=>
        val name = company.replace(" ","_")
        try {
          w2vModel.findSynonyms(name, 1)
          if (name.length < 60)  println(name)
        }catch {
          case e:Exception => s"$name not in vocabulary"
        }
    }




  }

}
