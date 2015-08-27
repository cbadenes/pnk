package es.upm.oeg.pnk.validators

import java.nio.charset.Charset

import org.apache.lucene.analysis.es.SpanishAnalyzer

/**
 * Created by cbadenes on 03/08/15.
 */
object WordValidator {

  def isValid (word: String): Boolean ={
    !word.isEmpty && word.forall(x=>isEncoded("UTF-8",x)) && word.exists(_.isLetter)
  }

  def isRelevant (word: String): Boolean ={
    isValid(word) && !isStopWord(word) && hasMinimumSize(word)
  }

  def isStopWord (word: String): Boolean ={
    SpanishAnalyzer.getDefaultStopSet.contains(word)
  }

  def isEncoded (charset: String, letter: Char): Boolean ={
    Charset.forName(charset).newEncoder().canEncode(letter)
  }

  def isAlphaNumeric(word: String): Boolean ={
    word.forall(_.isLetterOrDigit)
  }

  def isAlphabetical(word: String): Boolean ={
    word.forall(_.isLetter)
  }

  def hasMinimumSize(word: String): Boolean ={
    word.length > 3
  }

  def normalize (word: String): String ={
    if (word.contains(".")){
      val normalized =  word.split('.').map(normalize(_)).mkString(".")
      if (!word.startsWith(".") && word.endsWith(".")) return normalized + " ."
      if (word.endsWith(".")) return normalized + "."
      return normalized
    }
    var result = word.toLowerCase
    if ((word.forall(_.isUpper)) || (word.charAt(0).isUpper)) result = result.capitalize

    if (word.endsWith(",") && !word.startsWith(",")) return result.replace(",","") + " ,"
    return result
  }

}
