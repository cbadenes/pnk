package es.upm.oeg.pnk.actions

import es.upm.oeg.epnoi.matching.metrics.domain.entity._
import es.upm.oeg.epnoi.matching.metrics.domain.space.{ConceptsSpace, TopicsSpace}
import es.upm.oeg.epnoi.matching.metrics.feature.LuceneTokenizer
import es.upm.oeg.epnoi.matching.metrics.topics.LDASettings
import es.upm.oeg.pnk.transformations.Folder
import es.upm.oeg.pnk.validators.WordValidator
import org.apache.commons.lang.StringUtils
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.feature.Word2Vec
import org.apache.spark.rdd.RDD

/**
 * Created by cbadenes on 05/08/15.
 */
object ModelBuilder {

  def createW2V(sc: SparkContext, input: String, output: String): Unit = {

    println(s"creating or moving output folder '$output'..")
    Folder.moveIfExists(output)

    println(s"reading corpus from $input..")
    // one document per line
    val documents = sc.
      wholeTextFiles(input).
      filter(_._1.contains("part-00")).
      map(_._2).
      flatMap(line=>line.split("\\(file:.*/index.html,")).map(_.replace("\n","")).filter(!_.isEmpty)

    println(s"creating word2vec model in: $output..")
    val w2vAux = new Word2Vec()
    w2vAux.setVectorSize(500) // vector dimension (default 100)

    val counter = documents.flatMap(line=>line.split(" ")).filter(WordValidator.isRelevant).map(x=>(x,1)).reduceByKey((x,y)=>x+y).collectAsMap

    val corpus = documents.map(line=>line.split(" ").filter(x=>WordValidator.isRelevant(x) && counter.getOrElse(x,0)>5).toSeq)

    val w2vSimModel = w2vAux.fit(corpus)

    // save output
    w2vSimModel.save(sc,output)

  }


  def createTopics(sc: SparkContext, input: String, output: String): Unit = {

    println(s"creating or moving output folder '$output'..")
    Folder.moveIfExists(output)

    println(s"reading corpus from $input..")
    // one document per line
    val documents : RDD[(String,String)]= sc.wholeTextFiles(input).
      filter(_._1.contains("part-00")).
      flatMap(_._2.split("\n")).
      map(line=>line.split("/index.html,")).
      map(x=>(StringUtils.substringAfterLast(x(0),"/"),x(1)))


    // LDA Settings
    LDASettings.setTopics(6);
    LDASettings.setAlpha(4.1);
    LDASettings.setBeta(1.1);
    LDASettings.setMaxIterations(200);

    val author: Author  = Author("oeg.es/punica/author/000001","Sample","Author")

    // Regular Resources
    val regularResources : RDD[RegularResource] = documents.map{case x=>
      RegularResource(
        uri         = s"oeg.es/punica/tomo-"+x._1,
        url         = x._1,
        metadata    = Metadata(x._1,"2011",List(author)),
        bagOfWords  = LuceneTokenizer(x._2),
        resources   = Seq.empty)}

    println("regularResources size: " + regularResources.collect.size)

    // Conceptual Resources
    println("creating conceptual resources..")
    val conceptualResources : RDD[ConceptualResource] = regularResources.map(ConceptualResource(_))
    println("conceptualResources size: " + conceptualResources.collect.size)

    // Conceptual Space
    println("creating a conceptual space..")
    val conceptualSpace : ConceptsSpace = new ConceptsSpace(conceptualResources)

    println("creating the topics space ..")
    val topicSpace: TopicsSpace = new TopicsSpace(conceptualSpace)

    println("reading semantic resources..")
    val semanticResources : RDD[TopicalResource] = topicSpace.topicalResources;
    println("semanticResources size: " + semanticResources.collect.size)

    println("saving semantic resources..")
    semanticResources.saveAsObjectFile(output+"/lda-sr")

    // Distribution of words by topics
    val topics = topicSpace.model.ldaModel.describeTopics()

    val wordsOfTopics : Array[List[(String, Double)]] = topics.map(x=>(x._1.map(y=>conceptualSpace.vocabulary.wordsByKeyMap.getOrElse(y,"-error-")),x._2)).map(x=> x._1.toList.slice(0,50) zip x._2.toList.slice(0,50))

    println("WordsOfTopics: " + wordsOfTopics)

    // Create 'topics_words.csv' file
    val result  : RDD[String]= sc.parallelize(wordsOfTopics).map(list=>list.flatMap(element=>((element._1+" ") * (element._2*50000.0).toInt).split(" ") )).map(x=>x mkString(", "))

    println("Result: " + result)

    result.saveAsTextFile(output+"/lda-topics")

    sc.parallelize(wordsOfTopics).saveAsTextFile(output+"/lda-topics-2")

    // Show distribution of topics
    // [ [ {axis:"Topic 0",value:0.007757093003771018}, {axis:"Topic 1",value:0.007659624066343748}, {axis:"Topic 2",value:0.008174174819435527},{axis:"Topic 3",value:0.9312233837562842},{axis:"Topic 4",value:0.006225162364211153},{axis:"Topic 5",value:0.022121248317670206},{axis:"Topic 6",value:0.012167405951679025},{axis:"Topic 7",value:0.004671907720605327} ] ];
    semanticResources.foreach{case article=>
      val topics = article.topics.distribution.toList.zipWithIndex.map(x=>"{axis:\"Topic " + x._2+"\",value:" + x._1+"}")
      println(s"Topic Distribution from '"+article.conceptualResource.resource.uri+"': "+ "[ [ " + topics.mkString(", ") + "] ];")
    }

  }


  def main(args: Array[String]) : Unit ={

    val corpus : Array[List[(String, Double)]] = List(List(("a",0.5),("b",0.3)),List(("A",0.5),("B",0.3))).toArray


    val output : Array[List[String]] = corpus.map(list=>list.flatMap(element=>((element._1+" ") * (element._2*10.0).toInt).split(" ") ))

    val result = output.map(x=> x mkString(", "))

    for (i <- 0 until result.length){
      println("Topic " + i + ": " + result(i))
    }


  }

}
