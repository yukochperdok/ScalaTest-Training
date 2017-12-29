package com.stratio.examplesTest

/* Almacen */
trait Warehouse {
  def hasInventory(product: String, quantity: Int): Boolean
  def remove(product: String, quantity: Int)
}

/* Pedido de un almacen */
class Order(product: String, quantity: Int) {

  private var filled = false

  def fill(warehouse: Warehouse) {
    if (warehouse.hasInventory(product, quantity)) {
      warehouse.remove(product, quantity)
      filled = true
    }
  }

  def isFilled = filled
}
