package com.stratio.examplesTest

import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}
import org.scalatest.matchers.{BePropertyMatchResult, BePropertyMatcher}

class EmptyBePropertyMatcher extends BePropertyMatcher[MyStack[Int]] {
  def apply(left: MyStack[Int]) =
    BePropertyMatchResult(left.empty, "empty")
}
class FullBePropertyMatcher extends BePropertyMatcher[MyStack[Int]] {
  def apply(left: MyStack[Int]) =
    BePropertyMatchResult(left.full, "full")
}

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
    val emptyMyStack = emptyStack
    scenario("Check the empty stack") {

      Given("a empty stack")
      When("check if it's empty")
      Then("should be empty")
      emptyMyStack should be (propEmpty)
    }

    scenario("The empty stack is popped") {

      Given("a empty stack")
      When("pop element")
      Then("should be fail")
      And("should throws IllegalStateException")
      an [IllegalStateException] should be thrownBy {
        emptyMyStack.pop
      }
    }

    scenario("The empty stack is peeked") {

      Given("a empty stack")
      When("peek the last element")
      Then("should be fail")
      And("should throws IllegalStateException")
      an [IllegalStateException] should be thrownBy {
        emptyMyStack.peek
      }
    }

  }

  feature("Control the capacity on the full stack") {

    scenario("Check the full stack") {

      Given("a full stack")
      val fullMyStack = fullStack

      When("check if it's full")
      Then("should be full")
      fullMyStack should be (propFull)
    }

    scenario("The full stack is popped"){

      Given("a full stack")
      val fullMyStack = fullStack
      val initialSize = fullMyStack.size

      When("pop a element")
      fullMyStack.pop()

      Then("the stack should not be full")
      fullMyStack should not be (propFull)

      And("the stack's size should decrease one unit")
      fullMyStack.size should === (initialSize - 1)

      And("the the last element there should be")
      fullMyStack.size should !== (Nil)
    }

    scenario("The full stack is pushed"){

      Given("a full stack")
      val fullMyStack = fullStack

      When("push a element")

      Then("should be fail")
      And("should throws IllegalStateException")
      an [IllegalStateException] should be thrownBy {
        fullMyStack.push(5)
      }
    }

  }

  feature("Control the one-element stack") {

    scenario("Check state of stack") {

      Given("a one-element stack")
      val oneElementMyStack = stackWithOneItem

      When("check if it's full")
      Then("should not be full")
      oneElementMyStack should not be (propFull)

      When("check if it's empty")
      Then("should not be empty")
      oneElementMyStack should not be (propEmpty)
    }

    scenario("The one-element stack is added to the top") {

      Given("a one-element stack")
      val oneElementMyStack = stackWithOneItem
      val initialSize = oneElementMyStack.size
      And("a new element")
      val newElement = 7

      When("add the element to the top")
      oneElementMyStack.push(newElement)

      Then("the size should increase")
      oneElementMyStack.size should === (initialSize + 1)

      And("the element on the top of stack should be the new element")
      oneElementMyStack.peek should === (newElement)
    }
  }

  feature("Control the nearly-full stack") {

    scenario("Check state of nearly-full") {

      Given("a nearly-full stack")
      val nearlyFullMyStack = stackWithOneItemLessThanMaxCapacity

      When("check if it's full")
      Then("should not be full")
      nearlyFullMyStack should not be (propFull)

      When("check if it's empty")
      Then("should not be empty")
      nearlyFullMyStack should not be (propEmpty)
    }

    scenario("The the nearly-full stack is peeked") {

      Given("a nearly-full stack")
      val nearlyFullMyStack = stackWithOneItemLessThanMaxCapacity
      val initialSize = nearlyFullMyStack.size

      And("the last added element")
      val lastElement = lastItemAdded

      When("peek the top element")
      val topElement = nearlyFullMyStack.peek

      Then("the size should be the same")
      nearlyFullMyStack.size should === (initialSize)

      And("the element on the top of stack should be the last added element")
      topElement should be (lastElement)
    }

    scenario("The the nearly-full stack is popped") {

      Given("a nearly-full stack")
      val nearlyFullMyStack = stackWithOneItemLessThanMaxCapacity
      val initialSize = nearlyFullMyStack.size
      And("the last added element")
      val lastElement = lastItemAdded

      When("pop the top element")
      val topElement = nearlyFullMyStack.pop()

      Then("the size should not be the same")
      nearlyFullMyStack.size should !== (initialSize)

      And("the popped element should be the last added element")
      topElement should be (lastElement)

      And("the new element on the top of stack should not be the last added element")
      nearlyFullMyStack.peek should !== (lastElement)
    }
  }
}
