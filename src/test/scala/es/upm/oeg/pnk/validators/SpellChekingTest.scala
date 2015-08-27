package es.upm.oeg.pnk.validators

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class SpellChekingTest extends FunSuite {

  test("Spell Check Nouns") {

      println("Spell Validation of 'Maria'")
      assert(!SpellValidator.isValid("Maria"))

      println("Spell Validation of 'María'")
      assert(SpellValidator.isValid("María"))

      println("Spell Validation of 'Jose'")
      assert(!SpellValidator.isValid("Jose"))

      println("Spell Validation of 'José'")
      assert(SpellValidator.isValid("José"))

      println("Spell Validation of 'Marta'")
      assert(SpellValidator.isValid("Marta"))

      println("Spell Validation of 'Ana'")
      assert(SpellValidator.isValid("Ana"))

      println("Spell Validation of 'Miguel'")
      assert(SpellValidator.isValid("Miguel"))

      println("Spell Validation of 'Antonio'")
      assert(SpellValidator.isValid("Antonio"))

      println("Spell Validation of 'David'")
      assert(SpellValidator.isValid("David"))

      println("Spell Validation of 'Madrigal'")
      assert(SpellValidator.isValid("Madrigal"))

      assert(!SpellValidator.isValid("Marjaliza"))

  }

  test("Spell Check Places") {

    println("Spell Validation of 'Fiscalía'")
    assert(!SpellValidator.isValid("Fiscalía"))

    println("Spell Validation of 'empresa'")
    assert(!SpellValidator.isValid("empresa"))

    println("Spell Validation of 'Anticorrupción'")
    assert(!SpellValidator.isValid("Anticorrupción"))


  }

  test("Spell Check Adjectives") {

    println("Spell Validation of 'jurídicas'")
    assert(SpellValidator.isValid("jurídicas"))

    println("Spell Validation of 'Const'")
    assert(!SpellValidator.isValid("Const"))

  }


}
