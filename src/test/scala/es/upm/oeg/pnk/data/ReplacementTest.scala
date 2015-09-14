package es.upm.oeg.pnk.data

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class ReplacementTest extends FunSuite {

  val threshold = 0.8

  test("Negative Accuracy") {

    val input = "Marjaliza__,__Marjal__,__0.8984359358116174__,__1.0__,__0.9333333373069763__,__true__,__3383__,__5"
    val repl  = new Replacement(input)

    println("Accuracy for: " + input + " is: " + repl.accuracy)
    assert(repl.accuracy < threshold)
  }

  test("Positive Accuracy") {
    val input = "Depositos__,__Depósitos__,__1.7056495229876225__,__0.8222222328186035__,__0.9481481313705444__,__true__,__16__,__45"
    val repl  = new Replacement(input)

    println("Accuracy for: " + input + " is: " + repl.accuracy)
    assert(repl.accuracy > threshold)
  }

  test("Low Frequency") {
    val input = "legitimamente__,__legítimamente__,__3.2161811532583373__,__0.8769230842590332__,__0.944658100605011__,__true__,__65__,__17"
    val repl  = new Replacement(input)

    println("Accuracy for: " + input + " is: " + repl.accuracy)
    assert(repl.accuracy > threshold)
  }

  test("High Frequency") {
    val input = "Marquez__,__Mare__,__1.5738430725932033__,__0.75__,__0.8999999761581421__,__false__,__28__,__91"
    val repl  = new Replacement(input)

    println("Accuracy for: " + input + " is: " + repl.accuracy)
    assert(repl.accuracy < threshold)
  }

  test("Low Smith Waterman One") {
    val input = "Figar__,__amigo__,__1.147744945186165__,__0.6000000238418579__,__0.7333333492279053__,__true__,__24__,__135"
    val repl  = new Replacement(input)

    println("Accuracy for: " + input + " is: " + repl.accuracy)
    assert(repl.accuracy < threshold)
  }

  test("Low Smith Waterman Two") {
    val input = "Herran__,__Herrero__,__1.1541046857203088__,__0.6666666865348816__,__0.8476190567016602__,__true__,__18__,__22"
    val repl  = new Replacement(input)

    println("Accuracy for: " + input + " is: " + repl.accuracy)
    assert(repl.accuracy < threshold)
  }

  test("Very High Frequency") {
    val input = "Marjalizavillaseñor__,__Marjaliza__,__1.0849097445117555__,__1.0__,__0.8947368264198303__,__false__,__28__,__3383"
    val repl  = new Replacement(input)

    println("Accuracy for: " + input + " is: " + repl.accuracy)
    assert(repl.accuracy > threshold)
  }

  test("Zero values") {
    val input = "A__,__B__,__0.0__,__0.0__,__0.0__,__false__,__0__,__0"
    val repl  = new Replacement(input)

    println("Accuracy for: " + input + " is: " + repl.accuracy)
    assert(repl.accuracy == 0)
  }

  test("Visualize") {
    val input1 = List(
      "11adrid__,__Madrid__,__1.0464544783224043__,__0.8333333134651184__,__0.8492063879966736__,__true__,__8__,__11967",
      "11adrid__,__Madrid;__,__1.441985232571798__,__0.7142857313156128__,__0.8095238208770752__,__false__,__8__,__74",
      "11adrid__,__-madrid__,__0.9302441496653959__,__0.7142857313156128__,__0.8095238208770752__,__false__,__8__,__31",
      "1aadrid__,__11adrid__,__0.6620834541612162__,__0.7714285850524902__,__0.9142857789993286__,__false__,__7__,__8"
    )

    input1.foreach(x=>println("Accuracy for: " + x + " is: " + new Replacement(x).accuracy))

  }

}
