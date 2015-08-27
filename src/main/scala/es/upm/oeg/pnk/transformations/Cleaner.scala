package es.upm.oeg.pnk.transformations

import es.upm.oeg.pnk.validators.WordValidator

/**
 * Created by cbadenes on 08/08/15.
 */
object Cleaner {

  def apply (input: String): String = {
    println("cleaning text..")
    input.
      replace("\f","").
      replace("("," , ").
      replace(")"," , ").
      split("\n").
      map{case line=>
        if (line.length == 0) "."
        else line.split(" ").filter(WordValidator.isValid(_)).map(WordValidator.normalize(_)).mkString(" ")
      }.
      filter(_.length>0).
      mkString(" ")
  }

}
