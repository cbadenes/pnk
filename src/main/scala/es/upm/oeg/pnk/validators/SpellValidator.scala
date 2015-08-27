package es.upm.oeg.pnk.validators

import java.io.File

import com.swabunga.spell.engine.SpellDictionaryHashMap
import com.swabunga.spell.event.SpellChecker

import scala.io.Source._

/**
 * Created by cbadenes on 25/08/15.
 */
object SpellValidator {

  val dictionary    = new SpellDictionaryHashMap(new File("src/main/resources/spell/es_ES/es_ESx.dic"))
  val spellChecker  = new SpellChecker(dictionary)

  val maleNames     = fromFile("src/main/resources/nouns/es_male.txt").getLines.toList
  val femaleNames   = fromFile("src/main/resources/nouns/es_female.txt").getLines.toList

  def isValid(word: String): Boolean ={
    spellChecker.isCorrect(word) || maleNames.contains(word) || femaleNames.contains(word)
  }

}
