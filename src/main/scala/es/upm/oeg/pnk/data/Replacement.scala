package es.upm.oeg.pnk.data

import es.upm.oeg.pnk.validators.{SpellValidator, WordValidator}

/**
 * Created by cbadenes on 27/08/15.
 */
case class Replacement (token: String, similar: String, w2vMeasure: Double, smithWatermanMeasure: Double, jaroWinklerMeasure: Double, spell: Boolean, tokenFreq: Integer, similarFreq: Integer) {

  val SEPARATOR = "__,__"

  val accuracy = {
    val w2vrate = w2vMeasure / (3 + (w2vMeasure-3) + Math.abs(w2vMeasure-3))
    val swRate  = smithWatermanMeasure
    val jwRate  = jaroWinklerMeasure

    val highFreq = if (tokenFreq > similarFreq) tokenFreq.toDouble else similarFreq.toDouble
    val diffFreq = Math.abs(tokenFreq-similarFreq).toDouble + tokenFreq.toDouble
    val freqRate  = if (highFreq == 0.0 || diffFreq == 0.0) 0.0 else highFreq / diffFreq

//    val spellRate = if (spell) 0.5 else 0.0

//    (w2vrate + swRate + jwRate + freqRate + spellRate)/5
    (w2vrate + swRate + jwRate + freqRate)/4
  }

  def this(row: String) =
    this(
      row.split("__,__")(0),
      row.split("__,__")(1),
      row.split("__,__")(2).toDouble,
      row.split("__,__")(3).toDouble,
      row.split("__,__")(4).toDouble,
      row.split("__,__")(5).toBoolean,
      row.split("__,__")(6).toInt,
      row.split("__,__")(7).toInt
    )


  def this(token: String, similar:String) =
    this(
      token,
      similar,
      0.0,
      0.0,
      0.0,
      false,
      0,
      0
    )

  override def toString(): String={
    s"$token$SEPARATOR$similar$SEPARATOR$w2vMeasure$SEPARATOR$smithWatermanMeasure$SEPARATOR$jaroWinklerMeasure$SEPARATOR$spell$SEPARATOR$tokenFreq$SEPARATOR$similarFreq$SEPARATOR$accuracy"
  }

  def sameWord(): Boolean={
    token.equals(similar)
  }

  def isTokenValid(): Boolean={
    WordValidator.isValid(token)
  }

  def isSimilarWordValid(): Boolean={
    WordValidator.isValid(similar)
  }

  def areBothValids(): Boolean={
    isTokenValid && isSimilarWordValid
  }

  def isTokenCorrectlySpelled(): Boolean={
    SpellValidator.isValid(token)
  }

  def isSimilarWordCorrectlySpelled(): Boolean={
    SpellValidator.isValid(similar)
  }

  def areBothCorrectlySpelled(): Boolean={
    isTokenCorrectlySpelled && isSimilarWordCorrectlySpelled
  }

  def change(): Replacement={
    Replacement(similar,token,w2vMeasure,smithWatermanMeasure,jaroWinklerMeasure,SpellValidator.isValid(token),similarFreq,tokenFreq)
  }

  def newSimilar(newSimilar: String): Replacement ={
    //TODO Change all parameters, not only similar word!
    Replacement(token,newSimilar,w2vMeasure,smithWatermanMeasure,jaroWinklerMeasure,spell,similarFreq,tokenFreq)
  }
}
