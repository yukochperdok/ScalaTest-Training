package com.stratio.examplesTest

import org.scalatest.{Matchers, PropSpec}
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, TableDrivenPropertyChecks}
import scala.collection.immutable.{BitSet, HashSet, TreeSet}

/**
  * El estilo basado en propiedades es muy util cuando se quieren hacer tests
  * basado en propiedades (Property driven testing)
  * por ejemplo para las clases de utilidades.
  */

/**
  * Se puede definir una tabla que definira el juego de datos a chequear.
  * Y se recorera utilizando el forAll
  */
class MyPropSetSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val examples =
    Table(
      "set",
      BitSet.empty,
      HashSet.empty[Int],
      TreeSet.empty[Int]
    )

  property("an empty Set should have size 0") {
    forAll(examples) { set =>
      set.size should be (0)
    }
  }

  property("invoking head on an empty set should produce NoSuchElementException") {
    forAll(examples) { set =>
      a [NoSuchElementException] should be thrownBy { set.head }
    }
  }
}


class Fraction(n: Int, d: Int) {

  // Devuelve una IllegalArgumentException si no se cumple el requisito
  require(d != 0)
  require(d != Integer.MIN_VALUE)
  require(n != Integer.MIN_VALUE)

  val numer = if (d < 0) -1 * n else n
  val denom = d.abs

  override def toString = numer + " / " + denom
}

class MyTableDrivenFractionSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val invalidValues =
    Table(
      ("n",               "d"),
      (Integer.MIN_VALUE, Integer.MIN_VALUE),
      (1,                 Integer.MIN_VALUE),
      (Integer.MIN_VALUE, 1),
      (Integer.MIN_VALUE, 0),
      (1,                 0)
    )

  val validValues =
    Table(
      ("n", "d"),
      (  1,   2),
      ( -1,   2),
      (  1,  -2),
      ( -1,  -2),
      (  3,   1),
      ( -3,   1),
      (  3,  -1),
      ( -3,  -1)
    )

  property("invalid values in a fraction") {
    forAll(invalidValues) { (n: Int, d: Int) =>
      a [IllegalArgumentException] should be thrownBy { new Fraction(n, d) }
    }
  }

  property("all valid filtered values in a fraction") {
    forAll(validValues) { (n: Int, d: Int) =>
      val f = new Fraction(n, d)

      if (n < 0 && d < 0 || n > 0 && d > 0)
        f.numer should be > 0
      else if (n != 0)
        f.numer should be < 0
      else
        f.numer should === (0)

      f.denom should be > 0
    }
  }

  // O puedo utilizar una tabla con todos los posibles valores y luego filtrarla con whenever

  val allValues = invalidValues ++ validValues
  property("valid values in a fraction") {
    forAll(allValues) { (n: Int, d: Int) =>

      whenever(d != 0 && d != Integer.MIN_VALUE
        && n != Integer.MIN_VALUE) {

        val f = new Fraction(n, d)

        if (n < 0 && d < 0 || n > 0 && d > 0)
          f.numer should be > 0
        else if (n != 0)
          f.numer should be < 0
        else
          f.numer should === (0)

        f.denom should be > 0
      }

    }
  }

}


/**
  * Es bastante tedioso ir montandote una(s) Table(s) con todos los posibles valores,
  * y de hecho no los pruebas todos. Mejor dejarle ese trabajo a un generador
  */
class MyGeneratorDrivenFractionSpec extends PropSpec with GeneratorDrivenPropertyChecks with Matchers  {

  property("valid generated values in a fraction") {
    forAll { (n: Int, d: Int) =>

      whenever(d != 0 && d != Integer.MIN_VALUE
        && n != Integer.MIN_VALUE) {

        val f = new Fraction(n, d)

        if (n < 0 && d < 0 || n > 0 && d > 0)
          f.numer should be > 0
        else if (n != 0)
          f.numer should be < 0
        else
          f.numer should === (0)

        f.denom should be > 0
      }

    }
  }
}

/**
  * Con ScalaCheck ademas puedes generarte los valores como tu quieras. Normalmente es lo que se suele utilizar
  */
import org.scalacheck.Gen

class MyGeneratorDrivenFractionSpec2 extends PropSpec with GeneratorDrivenPropertyChecks with Matchers  {
  // validNumbers = [Integer.MIN_VALUE + 1, Integer.MAX_VALUE] se lo aplico a n
  val validNumers = for (n <- Gen.choose(Integer.MIN_VALUE + 1, Integer.MAX_VALUE)) yield n

  // validDenoms = [Integer.MIN_VALUE + 1 , 0) y (0, Integer.MAX_VALUE] se lo aplico a d
  val validDenoms = for (d <- validNumers if d != 0) yield d

  property("valid generated values in a fraction") {
    forAll(validNumers, validDenoms) { (n: Int, d: Int) =>

      /* YA NO HARIA FALTA:
      whenever(d != 0 && d != Integer.MIN_VALUE
        && n != Integer.MIN_VALUE) {
        */

        val f = new Fraction(n, d)

        if (n < 0 && d < 0 || n > 0 && d > 0)
          f.numer should be > 0
        else if (n != 0)
          f.numer should be < 0
        else
          f.numer should === (0)

        f.denom should be > 0
      /*
      }
      */

    }
  }
}

