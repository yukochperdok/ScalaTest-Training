package com.stratio.examplesTest

import org.scalatest.{FlatSpec, Tag}
import org.scalatest.tags.Slow

/**
  * Se pueden especificar tags para los test de forma que luego puedas lanzar solo los que te interesen.
  * Por ejemplo:
  *     testOnly -- -n ListTestTag
  *     testOnly -- -n org.scalatest.tags.Slow
  *
  * O para excluirlos:
  *
  *     testOnly -- -l ListTestTag
  *     testOnly -- -l org.scalatest.tags.Slow
  *
  * O incluso solo los de una specificacion excluyendo los que consideres:
  *
  *     testOnly *.ListSpec -- -l "org.scalatest.tags.Slow ListTestTag"
  *
  *
  *
  * Hay unas etiquetas base definidas bajo "org.scalatest.Tag." de las que se puede extender
  */

class ListSpec extends FlatSpec {

  /**
    * Defino mi primer subject: "An empty List"
    * Puedo utilizar should, can o must
    *
    */
  "An empty List" should "have size 0" in {
    assert(List.empty.size == 0)
  }

  it should "produce NoSuchElementException when head is invoked" in { // Defino otro test con el mismo subject: "An empty Set"
    intercept[NoSuchElementException] {
      List.empty.head
    }
  }
  /**
    * Para etiquetar un test como ignorado
    */
  ignore should "be empty" in {
    assert(List.empty.isEmpty)
  }
  it should "not be non-empty" ignore {
    assert(!List.empty.nonEmpty)
  }


  /**
    * Defino otro subject: "A non-empty List"
    * Por lo tanto ya no puedo utilizar it
    */
  "A non-empty List" can "have the correct size" in {
    assert(List(1, 2, 3).size == 3)
  }

  /**
    * Defino un test como pendiente
    */
  it should "return a contained value when head is invoked" is (pending)

  /**
    * Etiqueto un test con una etiqueta estandar, en este caso Slow
    */
  import org.scalatest.tagobjects.Slow
  it should "be non-empty" taggedAs(Slow) in {
    assert(List(1, 2, 3).nonEmpty)
  }

  object ListTestTag extends Tag("ListTestTag")

  it should "be non-empty and tagged" taggedAs(Slow, ListTestTag) in {
    assert(List(1, 2, 3).nonEmpty)
  }
}

/**
  * Marcar todos los test de una Suite con una etiqueta
  */
@Slow
class ListSlowSpec extends FlatSpec { /*code omitted*/ }