package es.upm.oeg.pnk.data

import es.upm.oeg.pnk.transformations.{Classifier, Folder}
import es.upm.oeg.pnk.validators.WordValidator
import org.apache.commons.lang.StringUtils
import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.Word2VecModel
import org.apache.spark.rdd.RDD

/**
 * Created by cbadenes on 05/08/15.
 */
object GraphBuilder {

  def apply(w2vModel : Word2VecModel, input: RDD[(String, Long)]): StringBuilder = {

    val idMap = input.collectAsMap

    val json = new StringBuilder()

    json.append("{\n")

    json.append("\"graphs\": [],\n")
    json.append("\"links\": [\n")

    val out : RDD[(Long, AnyVal)] = input.flatMap(x=>w2vModel.findSynonyms(x._1, 20).filter(y=>idMap.contains(y._1)).map(y=>(x._2,idMap.getOrElse(y._1,-1))))

    json.append(out.map(x=>"{\"source\": " +x._1 + ", \"target\": " + x._2 + "},").collect.toList mkString("\n"))

    json.deleteCharAt(json.length-1)
    json.append("\n],\n")

    val source = out.map(x=>(x._1,1)).reduceByKey((x,y)=>x+y).collectAsMap
    val target = out.map(x=>(x._2,1)).reduceByKey((x,y)=>x+y).collectAsMap

    val details: RDD[(String, Long, Int)] = input.map(x=>(x._1,x._2,source.getOrElse(x._2,0)+target.getOrElse(x._2,0))).cache

    val great = details.sortBy(x=>x._3,false).first

    println("Maximum: " + great)

    json.append("\"nodes\": [\n")
    details.collect.toList.foreach{case company =>
      //       json.append("\t{name: \""+company._1+"\"},\n")
      val score : Double = company._3.toDouble / great._3.toDouble
      val size : Double = (score.toDouble * 100.0) + 10.0
      json.append("\t{\"size\": "+size+", \"score\": "+score+", \"id\": \""+company._1+"\", \"type\": \"circle\"},\n")
    }
    json.deleteCharAt(json.length-2)
    json.append("],\n")

    json.append("\"directed\": true,\n")
    json.append("\"multigraph\": false\n")

    json.append("}\n")

  }

}
