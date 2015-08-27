package es.upm.oeg.pnk.actions

import java.io.{File, FileInputStream}

import com.swabunga.spell.engine.{SpellDictionaryHashMap, SpellDictionaryASpell}
import com.swabunga.spell.event.{StringWordTokenizer, SpellCheckEvent, SpellCheckListener, SpellChecker}
import es.upm.oeg.pnk.validators.SpellValidator
import opennlp.tools.postag.{POSModel, POSTaggerME}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class SpellChekingTest extends FunSuite {

  val dictionary    = new SpellDictionaryHashMap(new File("src/main/resources/spell/es_ES/es_ESx.dic"))

  val spellChecker  = new SpellChecker(dictionary)

  test("Spell Check Nouns") {

    assert(!SpellValidator.isValid("Maria"))
    assert(SpellValidator.isValid("María"))

    assert(!SpellValidator.isValid("Jose"))
    assert(SpellValidator.isValid("José"))

    assert(SpellValidator.isValid("Marta"))

    assert(SpellValidator.isValid("Ana"))

    assert(SpellValidator.isValid("Miguel"))

    assert(SpellValidator.isValid("Antonio"))

    assert(SpellValidator.isValid("David"))

  }

  test("Spell Check Places") {

    assert(!SpellValidator.isValid("Fiscalía"))

    assert(!SpellValidator.isValid("empresa"))

    assert(!SpellValidator.isValid("Anticorrupción"))


  }

  test("Spell Check Adjectives") {

    assert(SpellValidator.isValid("jurídicas"))
    assert(!SpellValidator.isValid("Const"))
    assert(SpellValidator.isValid("Madrigal"))


  }


}
