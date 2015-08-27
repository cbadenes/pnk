package es.upm.oeg.pnk.transformations

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class CleanerTest extends FunSuite {



  test("Clean text") {

    val input   : String  = "\fw\n\n\fJUZGADO CENTRAL INSTRUCCION\n\nFecha ...... :\n\nOF.REG! REP DOS.CENT. INSTRUCCION\n\nHora\n\nMADRID( MADRID)\n\nUsuario Pet:\n\nju7500\n\nPENAL\n\nUsuarlo Reg:\n\nju7500"
    val expected  : String  = "w . Juzgado Central Instruccion . Fecha . Of.Reg! Rep Dos.Cent . Instruccion . Hora . Madrid Madrid . Usuario Pet: . ju7500 . Penal . Usuarlo Reg: . ju7500"
    val obtained  : String  = Cleaner(input)


    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: " + obtained)

    assert(expected.equals(obtained))
  }

  test("Inline parenthesis text") {

    val input     : String  = "MADRID( MADRID)"
    val expected  : String  = "Madrid Madrid"
    val obtained  : String  = Cleaner(input)


    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: " + obtained)

    assert(expected.equals(obtained))
  }

  test("Final point text") {

    val input     : String  = "en Madrid."
    val expected  : String  = "en Madrid ."
    val obtained  : String  = Cleaner(input)


    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: " + obtained)

    assert(expected.equals(obtained))
  }

  test("Final colon text") {

    val input     : String  = "en Madrid,"
    val expected  : String  = "en Madrid ,"
    val obtained  : String  = Cleaner(input)


    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: " + obtained)

    assert(expected.equals(obtained))
  }

}
