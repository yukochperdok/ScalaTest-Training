package com.stratio.examplesTest

import org.scalatest.FlatSpecLike


class AssertSuite extends FlatSpecLike{

  /**
    * Los assert de ScalaTest son mas potentes y mas claros que los de Scala (en mensajes y en tipos).
    * Devuelven TestFailedException con mensajes de error muy claros
    */

  "Left" should "be equal to right " in {
    val left = 2
    val right = 1
    assert(left == right)
  }

  val (a, b, c, d) = (1, 2, 3, 4)
  val xs:List[Int] = List(1,2,3)
  val num = 1.0

  "a" should "be equal to b or c greater than or equal to d" in {
    assert(a == b || c >= d)
  }

  "The list" should "contains 4" in {
    //val xs:List[Int] = List(1,2,4)
    assert(xs.exists(_ == 4))
  }

  it should "contains element greater than 10" in {
    //val xs:List[Int] = List(1,2,3,12)
    assert(xs.exists(i => i > 10))
  }

  "num" should "be instance of Int" in {
    assert(num.isInstanceOf[Int])
  }

  "None" should "be defined" in {
    assert(None.isDefined)
  }

  "'hello'" should "starts with 'h' and 'goodbye' ends with 'y'" in {
    assert("hello".startsWith("h") && "goodbye".endsWith("y"))
    // With customize message
    assert("hello".startsWith("h") && "goodbye".endsWith("y"), " - Error en alguna condicion")
  }

  "Operation" should "return 2" in{
    val a = 5
    val b = 2
    assertResult(2) {
      a - b
    }
  }

  "Operation" should "return 3" in{
    val a = 5
    val b = 2
    assertResult(3) {
      a - b
    }

    fail("I've got a bad feeling about this")
  }

}

class AssertThrowsSuite extends FlatSpecLike{

  /**
    * Los assertThrows de ScalaTest te permiten controlar las excepciones e interceptarlas
    */

  val s: String = "test"

  "charAt" should "throw IndexOutOfBoundsException if you ask for non right index" in {
    assertThrows[IndexOutOfBoundsException] { // Result type: Assertion
      s.charAt(-1)
    }
  }

  it should "throw IndexOutOfBoundsException if you ask for non right index (option 2)" in {
    val caught = intercept[IndexOutOfBoundsException] { // Result type: IndexOutOfBoundsException
      s.charAt(-1)
    }
    info(caught.getMessage)
    assert(caught.getMessage.indexOf("-1") != -1)
  }
}

class CancelSuite extends FlatSpecLike{

  /**
    * Se pueden cancelar tests. Incluso asumiendo datos.
    * En el caso de cancelarse se lanza una excepcion org.scalatest.exceptions.TestCanceledException
    */

  "test" should "try to execute, but precondition doesn't exists" in {
    cancel("Database was down")
  }

  case class Database(available: Boolean)

  it should "try to execute, but precondition doesn't exists (with assume)" in {

    val db = Database(false)
    assume(db.available)
  }

}
