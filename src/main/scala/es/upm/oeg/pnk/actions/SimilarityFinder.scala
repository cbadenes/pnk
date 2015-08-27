package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.validators.WordValidator
import org.apache.spark.mllib.feature.Word2VecModel
import org.simmetrics.StringMetrics

/**
 * Created by cbadenes on 13/08/15.
 */
object SimilarityFinder {

  val WORD2VEC_THRESOLD       = 0.5 //0.6
  val SMITH_WATERMAN_THRESOLD = 0.6 //0.7
  val JARO_WINKLER_THRESOLD   = 0.7 //0.9

  val SMITH_WATERMAN_RECURSIVE_THRESOLD = 0.7 //0.7
  val JARO_WINKLER_RECURSIVE_THRESOLD   = 0.9 //0.9


  def apply(model: Word2VecModel, word: String): List[(String,(Double,Double,Double))] ={
    try {
      model.findSynonyms(word, 1000)
        .filter(x=>WordValidator.isValid(x._1))
        .filter(_._2 > WORD2VEC_THRESOLD)
        .map(x=>(x._1,(x._2,StringMetrics.smithWaterman.compare(word, x._1).toDouble,StringMetrics.jaroWinkler.compare(word, x._1).toDouble)))
        .filter(x => x._2._2  > SMITH_WATERMAN_THRESOLD)
        .filter(x => x._2._3  > JARO_WINKLER_THRESOLD)
        .toList
    }catch {
      case e:Exception => return List.empty[(String,(Double,Double,Double))]
    }
  }

  def recursive(model: Word2VecModel, word: String): List[(String,(Double,Double,Double))] ={
    recursiveAux(model,word,List(word))
  }

  def recursiveAux(model: Word2VecModel, word: String, visited: List[String]): List[(String,(Double,Double,Double))] ={
    try {
      val words = SimilarityFinder(model,word).filter(x=> !visited.contains(x._1))
      words ::: words.
        filter(x=>x._2._2 > SMITH_WATERMAN_RECURSIVE_THRESOLD).
        filter(x=>x._2._3 > JARO_WINKLER_RECURSIVE_THRESOLD).
        flatMap(x=>SimilarityFinder.recursiveAux(model,x._1,words.map(_._1) ::: visited))
    }catch {
      case e:Exception => return List.empty[(String,(Double,Double,Double))]
    }
  }


}
