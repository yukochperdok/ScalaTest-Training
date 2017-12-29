package com.stratio.examplesTest

import scala.collection.mutable.ArrayBuffer

case class Topping(name: String, price: Double)

/**
  * Pizza contains a set of sorted toppings. Each one has its own price
  */
class Pizza {

  /**
    * Array of toppings of pizza
    */
  private val toppings = new ArrayBuffer[Topping]

  /**
    * Add a new Topping to ArrayBuffer. If it already exists, isn't added again
    * @param t New added topping
    */
  def addTopping (t: Topping) { if(!toppings.contains(t)) toppings += t }

  /**
    * Remove a Topping on ArrayBuffer. If array is empty throws a Exception
    * @param t Removed topping
    * @throws IllegalStateException
    */
  def removeTopping (t: Topping) {
    if(toppings.isEmpty) throw new IllegalStateException("Pizza is empty")
    toppings -= t
  }

  /**
    * Get a list of toppings (not array)
    * @return java.util.List: Array of toppings transformed to List
    */
  def getToppings(): List[Topping] = toppings.toList

  /**
    * Get the accumulated price of all toppings
    * @return Double: Accumulated Price
    */
  def getPrice(): Double = toppings.foldLeft(0.0)((accum, element) => accum + element.price)
}


