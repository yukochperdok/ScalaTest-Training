package com.stratio.examplesTest

import org.scalatest.FlatSpec


class PizzaTests extends FlatSpec{

  val greenOlives = Topping("green olives",1.3)
  val onions = Topping("onions",3)

  "A New Pizza" should "have zero toppings" in {
    val pizza = new Pizza
    assert(pizza.getToppings.size === 0)
  }


  "A Pizza" should "have size equal 1 after to add a new topping" in {
    val pizza = new Pizza
    pizza.addTopping(greenOlives)
    assert(pizza.getToppings.size === 1)
  }


  it should "have size equal 0 after to remove a topping" in {
    val pizza = new Pizza

    pizza.addTopping(onions)
    assert(pizza.getToppings.size === 1)

    pizza.removeTopping(onions)
    assert(pizza.getToppings.size === 0)
  }


  it should "not remove a topping that doesn't exist" in {
    val pizza = new Pizza

    pizza.addTopping(onions)
    assert(pizza.getToppings.size === 1)

    pizza.removeTopping(greenOlives)
    assert(pizza.getToppings.size === 1)
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
    assert(pizza.getPrice === 4.3)
  }
}