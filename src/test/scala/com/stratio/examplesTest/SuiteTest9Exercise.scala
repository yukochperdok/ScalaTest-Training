/*
package com.stratio.examplesTest


import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
  * Ejercicio practico simulacion Acceptance Tests:
  *  Se utilizara la class FeatureSpec en combinacion con el trait GivenWhenThen
  *  para intentar simular unas pruebas de aceptacion.
  *  En combinacion a ello se pondra en practica los Matchers aprendidos en SuiteTest8.
  *  Intentando crear 2 PropertyMatcher para los atributos full y empty de la class MyStack.
  *  La dinamica de pruebas será muy similar a lo visto en SuiteTest7 sobre la class MyStack[Int]
  *  incluso se proporcionan los metodos para la generacion de emptyStack, fullStack, stackWithOneItem y
  *  stackWithOneItemLessThanMaxCapacity.
  */

class EmptyBePropertyMatcher

class FullBePropertyMatcher

class MyStackSpec extends FeatureSpec with GivenWhenThen with Matchers{

  info("As a Structure Data Architect")
  info("I want to be able to check new Stack's structure")
  info("So I'd like to push and pop elements in a LIFO-way")
  info("And to see the structure's last element and state")

  def emptyStack = new MyStack[Int]

  def fullStack = {
    val stack = new MyStack[Int]
    for (i <- 0 until stack.MAX)
      stack.push(i)
    stack
  }

  val lastItemAdded:Int = 9

  def stackWithOneItem = {
    val stack = new MyStack[Int]
    stack.push(lastItemAdded)
    stack
  }

  def stackWithOneItemLessThanMaxCapacity = {
    val stack = new MyStack[Int]
    for (i <- 1 to lastItemAdded)
      stack.push(i)
    stack
  }

  val propEmpty = new EmptyBePropertyMatcher
  val propFull = new FullBePropertyMatcher


  feature("Control the empty stack") {
    scenario("Check the empty stack") {

      Given("a empty stack")
      When("check if it's empty")
      Then("should be empty")
    }

    scenario("The empty stack is popped") {

      Given("a empty stack")
      When("pop element")
      Then("should be fail")
      And("should throws IllegalStateException")
    }

    scenario("The empty stack is peeked") {

      Given("a empty stack")
      When("peek the last element")
      Then("should be fail")
      And("should throws IllegalStateException")
    }

  }

  feature("Control the capacity on the full stack") {

    scenario("Check the full stack") {

      Given("a full stack")

      When("check if it's full")
      Then("should be full")
    }

    scenario("The full stack is popped"){

      Given("a full stack")

      When("pop a element")

      Then("the stack should not be full")

      And("the stack's size should decrease one unit")

      And("there should be the last element")
    }

    scenario("The full stack is pushed"){

      Given("a full stack")

      When("push a element")

      Then("should be fail")
      And("should throws IllegalStateException")

    }

  }

  feature("Control the one-element stack") {

    scenario("Check state of stack") {

      Given("a one-element stack")

      When("check if it's full")
      Then("should not be full")

      When("check if it's empty")
      Then("should not be empty")

    }

    scenario("The one-element stack is added to the top") {

      Given("a one-element stack")

      And("a new element")


      When("add the element to the top")

      Then("the size should increase")

      And("the element on the top of stack should be the new element")

    }
  }

  feature("Control the nearly-full stack") {

    scenario("Check state of nearly-full") {

      Given("a nearly-full stack")

      When("check if it's full")
      Then("should not be full")

      When("check if it's empty")
      Then("should not be empty")

    }

    scenario("The the nearly-full stack is peeked") {

      Given("a nearly-full stack")

      And("the last added element")

      When("peek the top element")

      Then("the size should be the same")

      And("the element on the top of stack should be the last added element")

    }

    scenario("The the nearly-full stack is popped") {

      Given("a nearly-full stack")

      And("the last added element")

      When("pop the top element")

      Then("the size should not be the same")

      And("the popped element should be the last added element")

      And("the new element on the top of stack should not be the last added element")

    }
  }
}
*/