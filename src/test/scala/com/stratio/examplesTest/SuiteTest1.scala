package com.stratio.examplesTest

import java.util.{EmptyStackException, Stack}

import org.scalatest.{FlatSpecLike, Matchers}


class SuiteTest1 extends FlatSpecLike
                       with Matchers {



  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be (2)
    stack.pop() should be (1)
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[Int]
    a [EmptyStackException] should be thrownBy {
      emptyStack.pop()
    }
  }
}
