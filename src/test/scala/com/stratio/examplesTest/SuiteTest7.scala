package com.stratio.examplesTest

import org.scalatest.FlatSpec

/**
  * A veces lo que nos interesa no es compartir fixtures, sino tests, que puedan ser utilizados
  * en diferentes fixtures o diferentes spec
  */

trait MyEmptyStackBehaviors { this: FlatSpec =>

  def nonEmptyStack(newStack: MyStack[Int], lastItemAdded: Int) {

    it should "be non-empty" in {
      assert(!newStack.empty)
    }

    it should "return the top item on peek" in {
      assert(newStack.peek === lastItemAdded)
    }

    it should "not remove the top item on peek" in {
      val stack = newStack
      val size = stack.size
      assert(stack.peek === lastItemAdded)
      assert(stack.size === size)
    }

    it should "remove the top item on pop" in {
      val stack = newStack
      val size = stack.size
      assert(stack.pop === lastItemAdded)
      assert(stack.size === size - 1)
    }
  }
}

trait MyFullStackBehaviors { this: FlatSpec =>


  def nonFullStack(newStack: MyStack[Int]) {

    it should "not be full" in {
      assert(!newStack.full)
    }

    it should "add to the top on push" in {
      val stack = newStack
      val size = stack.size
      stack.push(7)
      assert(stack.size === size + 1)
      assert(stack.peek === 7)
    }
  }

  def fullStack(newStack: MyStack[Int]) {

    it should "be full" in {
      assert(newStack.full)
    }

    it should "complain on a push" in {
      intercept[IllegalStateException] {
        newStack.push(10)
      }
    }
  }
}

class SharedTestSpec extends FlatSpec with MyEmptyStackBehaviors with MyFullStackBehaviors {

  /**
    * Test propios
    */
  def emptyStack = new MyStack[Int]

  "A Stack (when empty)" should "be empty" in {
    assert(emptyStack.empty)
  }

  it should "complain on peek" in {
    intercept[IllegalStateException] {
      emptyStack.peek
    }
  }

  it should "complain on pop" in {
    intercept[IllegalStateException] {
      emptyStack.pop
    }
  }

  /**
    * Test heredados del comportamiento definido por MyEmptyStackBehaviors y MyFullStackBehaviors
    */
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



  "A Stack (with one item)" should behave like nonEmptyStack(stackWithOneItem, lastItemAdded)

  it should behave like nonFullStack(stackWithOneItem)

  "A Stack (with one item less than capacity)" should behave like nonEmptyStack(stackWithOneItemLessThanMaxCapacity, lastItemAdded)

  it should behave like nonFullStack(stackWithOneItemLessThanMaxCapacity)

  "A Stack (full)" should behave like fullStack(fullStack)

  it should behave like nonEmptyStack(fullStack, lastItemAdded)
}