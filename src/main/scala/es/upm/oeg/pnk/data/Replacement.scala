package es.upm.oeg.pnk.data

/**
 * Created by cbadenes on 27/08/15.
 */
case class Replacement (token: String, similar: String, w2vMeasure: Double, smithWatermanMeasure: Double, jaroWinklerMeasure: Double, spell: Boolean, tokenFreq: Integer, similarFreq: Integer) {

  val SEPARATOR = "__,__"

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
    s"$token$SEPARATOR$similar$SEPARATOR$w2vMeasure$SEPARATOR$smithWatermanMeasure$SEPARATOR$jaroWinklerMeasure$SEPARATOR$spell$SEPARATOR$tokenFreq$SEPARATOR$similarFreq"
  }

  def sameWord(): Boolean={
    token.equals(similar)
  }

}
