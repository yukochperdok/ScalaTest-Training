package com.stratio.examplesTest

import org.scalatest.FlatSpec


class PizzaTests extends FlatSpec{

  val greenOlives = Topping("green olives",1.3)
  val onions = Topping("onions",3)

  "A New Pizza" should "have zero toppings" in {
    val pizza = new Pizza
    assert(pizza.getToppings.size === 0)
  }


  "A Pizza" should "increase one-unit size after to add a new topping" in {
    val pizza = new Pizza
    val originalSize = pizza.getToppings().size
    pizza.addTopping(greenOlives)
    assert(originalSize + 1 === pizza.getToppings.size)
  }


  it should "decrease one-unit size after to remove a topping" in {
    val pizza = new Pizza
    pizza.addTopping(onions)
    val originalSize = pizza.getToppings().size
    pizza.removeTopping(onions)
    assert(originalSize - 1 === pizza.getToppings.size)
  }


  it should "not remove a topping that doesn't exist" in {
    val pizza = new Pizza

    pizza.addTopping(onions)
    val firstSize = pizza.getToppings().size

    pizza.removeTopping(greenOlives)
    assert(pizza.getToppings.size === firstSize)
  }


  "A Empty Pizza" should "fail when one topping is removed" in {
    val pizza = new Pizza

    assertThrows[IllegalStateException] {
      pizza.removeTopping(greenOlives)
    }
  }


  "A Pizza" should "return the price of all its toppings" in {
    val pizza = new Pizza
    pizza.addTopping(onions)
    pizza.addTopping(greenOlives)
    assert(pizza.getPrice === onions.price + greenOlives.price)
  }
}