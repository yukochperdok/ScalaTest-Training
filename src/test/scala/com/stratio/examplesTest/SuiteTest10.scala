package com.stratio.examplesTest

import org.scalatest.{FlatSpec, Matchers}
import org.scalamock.scalatest.MockFactory

/**
  * La mejor forma de simular objetos, traits, incluso funciones => Mocks
  * Aqui utilizaremos ScalaMocks que viene bastante integrado con ScalaTest,
  * pero en cualquier tipo de Suite de ScalaTest se pueden utilizar otros framworks
  * para el mockeo de Java class/interfaces:
  *   1. EasyMock
  *   2. JMock
  *   3. Mockito
  *
  * incluso mezclandolos con los ScalaMocks.
  */

class FunctionMockSuite extends FlatSpec with Matchers with MockFactory{
  /**
    * Function Mocks:
    * 1. recibe un Int y devuelve un String
    * 2. Se definen para varios valores experados varios valores devueltos
    */
  val intToStringMock = mockFunction[Int, String]
  inSequence {
    intToStringMock expects (1) returning "one" once;
    intToStringMock expects (2) returning "two" once;
    intToStringMock expects (3) returning "three" once;
  }

  // Me puede servir para testear la funcion map
  "map function" should "distribute each integer value" in {
    Seq(1,2,3) map intToStringMock shouldBe(Seq("one","two","three"))
  }
}

/**
  * Suponiendo que el trait Warehouse y sus metodos hasInventory y remove pueden
  * tener dependencias con un TransactionManager o cualquier tipo de SQL o NoSQL.
  * Y solo queremos probar el class Order y su metodo fill, podemos mockear Objects => Warehouse
  */
class ObjectMockTest extends FlatSpec with Matchers with MockFactory {

  "An order in stock" should "remove inventory" in {
    val mockWarehouse = mock[Warehouse]
    inSequence {
      (mockWarehouse.hasInventory _) expects ("Talisker", 50) returning true
      (mockWarehouse.remove _) expects ("Talisker", 50)
    }

    val order = new Order("Talisker", 50)
    // Con expected mock necesitas llamar a los metodos mockeados. Sino fallara el test
    order.fill(mockWarehouse)

    order should be a ('filled)
    // Otra opcion --> assert(order.isFilled)
  }


  "Out of stock" should "remove nothing" in {
    val mockWarehouse = mock[Warehouse]
    (mockWarehouse.hasInventory _) stubs (*, *) returning false

    val order = new Order("Talisker", 50)
    // Con stubbed mock no necesitas obligatoriamente llamar al metodo.
    order.fill(mockWarehouse)

    order should not be a ('filled)
    // Otra opcion --> assert(!order.isFilled)
  }
}