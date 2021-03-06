package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.transformations.Cleaner
import org.apache.spark.rdd.RDD
import org.jsoup.Jsoup

import scala.collection.Map

/**
  * Created by cbadenes on 09/08/15.
  */
object Parser {

   def apply(input: RDD[(String,String)]): RDD[(String,String)] = {
     val counter = input.flatMap(x=>x._2.split(" ")).map(x=>(x,1)).reduceByKey((x,y)=>x+y).collectAsMap
     input
       .map(x=>(x._1,Jsoup.parse(x._2,"utf-8").getElementsByTag("pre").text()))
       .map(x=>(x._1,Cleaner.byFreq(x._2,counter)))
   }
 }
