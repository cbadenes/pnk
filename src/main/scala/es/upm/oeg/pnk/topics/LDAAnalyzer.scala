package es.upm.oeg.pnk.topics

import es.upm.oeg.epnoi.matching.metrics.domain.entity.ConceptualResource
import es.upm.oeg.epnoi.matching.metrics.domain.space.{ConceptsSpace, TopicsSpace}

import scala.collection.Map
import scala.collection.mutable.ListBuffer

/**
 * Created by cbadenes on 04/08/15.
 */
object LDAAnalyzer {

  def words(model: (ConceptsSpace,TopicsSpace), num:Integer): Map[Integer,List[(String,Double)]]={
    var wordsByTopic : Map[Integer,List[(String,Double)]] = Map.empty
    val topics: Array[(Array[Int], Array[Double])] = model._2.model.ldaModel.describeTopics()
    var index: Integer = 0
    topics.foreach{ t =>
      var terms = new ListBuffer[(String,Double)]()
      for (x <- 0 until num){
        val tuple : (String,Double) = (model._1.vocabulary.wordsByKeyMap.getOrElse(t._1(x),"unknown"),t._2(x))
        terms  += tuple
      }
      wordsByTopic += (index -> terms.toList)
      index +=1
    }
    return wordsByTopic
  }

  def documents(model: (ConceptsSpace,TopicsSpace)): Map[String,List[Double]]={
    val resourcesMap: Map[Long, ConceptualResource] = model._1.conceptualResourcesMap.collectAsMap();
    return model._2.model.ldaModel.topicDistributions.map{case (id,v) => (resourcesMap.getOrElse(id,"unknown").toString,v.toArray.toList)}.collectAsMap()
  }

}
