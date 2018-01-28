package com.stratio.examplesTest

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}

object Addition {
  def add(a: Int, b: Int): Int = a + b
}

class AdditionPropSpec extends PropSpec with GeneratorDrivenPropertyChecks with Matchers {

  /*
   * Utilizando la funcion add debemos demostrar que sumar 0 a cualquier numero tiene como resultado
   * el numero original.
   */
  property("Adding 0 to a number always returns the original number") {
    forAll((n: Int) => Addition.add(n, 0) should equal(n))
  }

  /*
   * Aqui demostraremos, utilizando la funcion add, la relacion entre suma y multiplicacion
   * (utilizando el caracter '*' de la std library)
   */
  property("Addition is related to multiplication") {
    // Demostrar la propiedad de que cualquier numero sumado por si mismo es igual duplicar el numero (multiplicarlo por 2)
    forAll((n: Int) => Addition.add(n, n) should equal(2 * n))

    // Demostrar la propiedad de que cualquier numero sumado por si mismo 3 veces es igual triplicar el numero (multiplicarlo por 3)
    forAll((n: Int) => Addition.add(Addition.add(n, n), n) should equal(3 * n))
  }

  /*
   * Podemos pasar varios argumentos a nuestra funcion y ScalaCheck generara ramdom para esos argumentos: a y b
   *
   * Demostrar la propiadad de simetria de la funcion add, restando a la suma de a y b, el segundo elemento y demostrando
   * que es igual al primero (utilizando la resta de la std library '-')
   */
  property("Adding two numbers & taking one away again is the first number") {
    forAll((a: Int, b: Int) => (Addition.add(a, b) - b) should equal(a))
  }

  /*
   * Demostrar que dada la suma de dos numeros cualquiera, ya bien sea restandole la misma suma o restandole cada uno de los
   * operandos separados el resultado es 0 (utilizando la resta de la std library '-')
   */
  property("Adding two numbers & taking them both away is zero") {
    forAll((a: Int, b: Int) => (Addition.add(a, b) - a - b) should equal(0))
  }
  property("Adding two numbers & taking their sum away is zero") {
    forAll((a: Int, b: Int) => (Addition.add(a, b) - Addition.add(a, b)) should equal(0))
  }
}
