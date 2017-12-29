package com.stratio.examplesTest

import java.io.File
import java.util

import org.scalatest.matchers.{MatchResult, Matcher}
import org.scalatest.{FlatSpec, Matchers}

/**
  * El MatcherSuite te proporciona una opcion mas BDD que los assert (y por ello se convierte en menos "verboso")
  * Pero ademas te permite muchas mas comprobaciones
  */
class MatcherEqualitySuite extends FlatSpec with Matchers{
  def incr(i: Int) = i + 1

  val result = incr(2)

  /**
    * Comparadores de Igualdad
    */

  result should equal (3) // Por defecto, testea left == right, excepto para arrays
  result should be (3)    // Por defecto, testea left == right, excepto para arrays
  result should === (3)   // Por defecto, testea left === right, excepto para arrays.
                          // operator ===, compara el tipado de left y right en tiempo de compilacion, luego compara el contenido.

  result shouldEqual 3 // Otras alternativas sin uso de parentesis
  result shouldBe 3

  // Para array se utiliza lo mismo pero en realidad compara left.deep == right.deep
  // Se compara estructuralmente. A efectos practicos es igual.
  Array(1, 2) should equal (Array(1, 2))
  Array(1, 2) should be (Array(1, 2))
  Array(1, 2) should === (Array(1, 2))


  // El operator === permite pasarle parametros implicitos o explicitos de igualdad: Equality[L]
  // L seria el tipo de left (es decir el tipo que recoja greeting)

  val greeting = "Hi"
  //greeting should equal ("hi") // Ambos fallarian: "[H]i" did not equal "[h]i"
  //greeting should === ("hi")

  //Puedo utilizar lowerCased que me da un Equality[String] y me convierte a lowercase la cadena:

  // De forma explicita
  import org.scalactic.StringNormalizations._

  greeting should equal ("hi") (after being lowerCased)

  // De forma implicita, recogiendo el Equality[String] y sera parametro de equal o ===
  implicit val strEq = lowerCased.toEquality
  greeting should equal ("hi")
  greeting should === ("hi")

  /**
    * Comparadores de Desigualdad
    */
  greeting should not equal "bye"
  greeting should not be "such sweet sorrow"
  greeting should !== ("ho")                 // !== es el negativo de ===
}

class MatcherIdentitySuite extends FlatSpec with Matchers{

  /**
    * Comparar instancias: exactamente los mismos objetos
    */
  val list = List(1, 2, 3)
  val ref1 = list
  val ref2 = list

  ref1 should be theSameInstanceAs ref2


  /**
    * Comparar tipos
    */
  class Tiger
  class Orangutan
  val tiger = new Tiger

  tiger shouldBe a [Tiger] // IMP: should debe acompañarse de a o an
  tiger should not be an [Orangutan]

  val tigerList = List(tiger)
  tigerList shouldBe a [List[Tiger]]
  tigerList shouldBe a [List[_]]     // es recomandable usar underscore (_) en estas comparaciones
}

class MatcherExceptionsSuite extends FlatSpec with Matchers{

  /**
    * Testeo de excepciones
    */
  val s = "hai"
  // Asegurarse que exception es lanzada
  an [IndexOutOfBoundsException] should be thrownBy {
    s.charAt(-1)
  }
  // Capturar la excepcion en una variable
  val caught = the [IndexOutOfBoundsException] thrownBy {
    s.charAt(-1)
  }
  // Inspeccionar una excepcion lanzada
  the [IndexOutOfBoundsException] thrownBy {
    s.charAt(-1)
  } should have message ("String index out of range: -1")
}

class MatcherSizeSuite extends FlatSpec with Matchers{
  val string = "hai"
  val map = Map(1 -> "1", 2 -> "2")
  /**
    * Testear longitud: Funciona para cualquier estructura que tenga el
    * campo length o un metodo getLength. El valor devuelto puede ser Int o Long
    */
  string should have length 3


  /**
    * Testear tamaño: Funciona para cualquier estructura que tenga el
    * campo size o un metodo getSize. El valor devuelto puede ser Int o Long
    */
  map should have size 2
}


class MatcherStringsSuite extends FlatSpec with Matchers{

  /**
    * Testeo sobre cadenas: inicio y final de cadenas, trabajo con RE
    */

  val string = "Hello world"
  string should startWith ("Hello")

  string should endWith ("world")

  string should include ("wor")

  string should startWith regex "Hel*o"
  string should startWith regex "Hel*o".r         // funciona tanto comparando con String como con Regex

  string should endWith regex "wo.ld".r           // funciona tanto comparando con String como con Regex

  string should include regex "wo.ld".r           // funciona tanto comparando con String como con Regex

  val stringDouble = "-5.6"
  val rex = """(-)?(\d+)(\.\d*)?""".r
  stringDouble should fullyMatch regex rex
}

class MatcherOrderRangesSuite extends FlatSpec with Matchers{
  /**
    * Testeo greater and less than: Vale para cualquier T si existe un implicito Ordering[T]
    */

  val one = 1
  one should be < 7
  one should be <= 7
  one should be >= 0

  /**
    * Testeo rangos
    */
  val seven = 7
  seven should (be >= 4 and be <= 8)
  seven should be (6 +- 2)
  7.0f should be (6.9f +- 0.2f)
}

class MatcherBooleanSuite extends FlatSpec with Matchers{

  /**
    * Testeo de propiedades booleans (via reflection)
    */

  Set(1, 2, 3) should be a 'nonEmpty // !Set(1, 2, 3).isEmpty

  import java.io.File
  val tmp = new File("/tmp")
  tmp should be a 'directory // tmp.isDirectory

  import java.awt._
  import event.KeyEvent
  val canvas = new Canvas
  val keyEvent = new KeyEvent(canvas, 0, 0, 0, 0, '0')
  keyEvent should not be an ('actionKey) // keyEvent.isActionKey

  //Te puedes definir tu propio match de una propiedad (que devuelva Boolean)
  import org.scalatest.matchers.{BePropertyMatchResult, BePropertyMatcher}
  class FileBePropertyMatcher extends BePropertyMatcher[java.io.File] {
    def apply(left: File) =
      BePropertyMatchResult(left.isFile, "file") // No tiene porque cumplir el patron isProperty
  }
  val file = new FileBePropertyMatcher
  val temp2 = new File("file.txt")
  temp2 should not be file //!temp2.isFile (via property)
}

class MatcherArbitrarySuite extends FlatSpec with Matchers{
  /**
    * Testeo tipo 'Be' de propiedades (mediante BeMatcher)
    */
  import org.scalatest.matchers.{MatchResult,BeMatcher}
  // Incluso properties que no son booleans
  class OddMatcher extends BeMatcher[Int] {
    def apply(left: Int) =
      MatchResult(
        left % 2 == 1,
        left.toString + " was even",
        left.toString + " was odd"
      )
  }
  val odd = new OddMatcher
  7 should be (odd)



  /**
    * Testeo tipo 'Have' de propiedades (mediante HavePropertyMatcher)
    */

  // Por defecto puedes chequear el valor de las properties que debe tener una case class (utilizando HavePropertyMatcherGenerator)
  case class Book(title: String, author: String, pubYear: Int)
  val book = Book("A Book", "Sally", 2008)

  book should have (
    'title ("A Book"),
    'author ("Sally"),
    'pubYear (2008)
  )

  // Ademas se pueden ah-doc con tu propio HavePropertyMatcher
  import org.scalatest.matchers.{HavePropertyMatchResult,HavePropertyMatcher}

  def title(expectedValue: String) = new HavePropertyMatcher[Book, String] {
    def apply(book: Book) = HavePropertyMatchResult(
      book.title.toUpperCase == expectedValue.toUpperCase,
      "title",
      expectedValue,
      book.title
    )
  }
  def author(expectedValue: String) = new HavePropertyMatcher[Book, String] {
    def apply(book: Book) = HavePropertyMatchResult(
      book.author.toUpperCase == expectedValue.toUpperCase,
      "author",
      expectedValue,
      book.author
    )
  }

  book should have (
    title ("A BOOK"),
    author ("SALLY")
  )

  book should have (
    title ("A Book"),
    author ("Sally")
  )

}

class MatcherEmptySuite extends FlatSpec with Matchers{

  /**
    * Testeo emptyness
    */

  // Por reflection (menos optimo)
  List.empty should be a 'empty // List.empty.isEmpty

  // Para cualquier tipo GenTraversable
  List.empty shouldBe empty

  // Otros modos de comprobar si una estructura es vacia
  List.empty[Int] should be (Nil)
  Map.empty[Int, String] should be (Map())
  Set.empty[Int] should be (Set.empty)

  // Para tipos Option
  None shouldBe empty

  // Para tipos String
  "" shouldBe empty

  // Para cualquier tipo java.util.Map
  new java.util.HashMap[Int, Int] shouldBe empty

  // Para cualquier tipo estructural
  new { def isEmpty = true } shouldBe empty

  // Para tipos Array
  Array.empty shouldBe empty

}

class MatcherContainSuite extends FlatSpec with Matchers{

  /**
    * Testeo contain basico: contiene algun elemento??
    */

  List(1, 2, 3) should contain (2)

  Map('a' -> 1, 'b' -> 2, 'c' -> 3) should contain ('b' -> 2)

  Set(1, 2, 3) should contain (2)

  Array(1, 2, 3) should contain (2)

  "123" should contain ('2')

  Some(2) should contain (2)

  //List("Hi", "Di", "Ho") should contain ("ho") // Falla por que no es capaz de convertir, necesitas utilizar un Equality[String]
  import org.scalactic.StringNormalizations._
  (List("Hi", "Di", "Ho") should contain ("ho")) (after being lowerCased)

  // Con collections puedes preguntar por key o por value
  val set = new util.HashSet[String]
  set.add("three")
  set.add("five")
  set.add("seven")
  set should contain ("five")

  val map1 = new util.HashMap[Int, String]
  map1.put(1, "one")
  map1.put(2, "two")
  map1.put(3, "three")
  map1 should contain key (1)

  val map2 = new util.HashMap[Int, String]
  map2.put(1, "Hi")
  map2.put(2, "Howdy")
  map2.put(3, "Hai")
  map2 should contain value ("Howdy")


  /**
    * Testeo contain oneOf: contiene exactamente alguno de los elementos??
    */
  List(1, 2, 3, 4, 5) should contain oneOf (5, 7, 9)
  Some(7) should contain oneOf (5, 7, 9)
  "howdy" should contain oneOf ('a', 'b', 'c', 'd')

  // Si contiene mas de 1 elemento fallaria
  //List(1, 2, 3) should contain oneOf (2, 3, 4) // List(1, 2, 3) did not contain one (and only one) of (2, 3, 4)
  // Habria que utilizar atLeastOneOf
  List(1, 2, 3) should contain atLeastOneOf (2, 3, 4)

  (Array("Doe", "Ray", "Me") should contain oneOf ("X", "RAY", "BEAM")) (after being lowerCased)


  /**
    * Testeo contain noneOf: NO contiene ninguno de los elementos??
    */
  List(1, 2, 3, 4, 5) should contain noneOf (7, 8, 9)
  Some(0) should contain noneOf (7, 8, 9)
  "12345" should contain noneOf ('7', '8', '9')

  /**
    * Testeo contain atLeastOneOf: Al menos contiene uno de los elementos??
    */
  List(1, 2, 3) should contain atLeastOneOf (2, 3, 4)
  Array(1, 2, 3) should contain atLeastOneOf (3, 4, 5)
  "abc" should contain atLeastOneOf ('c', 'a', 't')

  // Tambien se puede utilizar un Equality[String]
  (Vector(" A", "B ") should contain atLeastOneOf ("a ", "b", "c")) (after being lowerCased and trimmed)

  /**
    * Testeo contain atMostOneOf: Contiene la mayoria de los elementos??
    */
  List(1, 2, 3, 4, 5) should contain atMostOneOf (5, 6, 7)

  /**
    * Testeo contain allOf: Contiene todos los elementos??
    */
  List(1, 2, 3, 4, 5) should contain allOf (2, 3, 5)

  /**
    * Testeo contain only: Contiene unicamente estos elementos??
    */
  List(1, 2, 3, 2, 1) should contain only (1, 2, 3)

  /**
    * Testeo contain theSameElementsAs: Contiene exactamente todos los elementos, independientemente de la estructura??
    */
  List(1, 2, 2, 3, 3, 3) should contain theSameElementsAs Vector(3, 2, 3, 1, 2, 3)
  List(1, 2, 2, 3, 3, 3) should contain theSameElementsAs List(3, 2, 3, 1, 2, 3)

  /**
    * Testeo contain inOrderOnly: Contiene unicamente estos elementos EN EL MISMO ORDEN??
    */
  List(1, 2, 2, 3, 3, 3) should contain inOrderOnly (1, 2, 3)

  /**
    * Testeo contain inOrder: Contiene estos elementos EN EL MISMO ORDEN??
    */
  List(0, 1, 2, 2, 99, 3, 3, 3, 5) should contain inOrder (1, 2, 3)

  /**
    * Testeo contain theSameElementsInOrderAs: Contiene exactamente todos los elementos EN EL MISMO ORDEN, independientemente de la estructura??
    */
  List(1, 2, 3) should contain theSameElementsInOrderAs collection.mutable.TreeSet(3, 2, 1)
  // No falla porque el orden del TreeSet se define de hoja a tallo


  //Puedes preguntar si un sorteable collection (Arrays, Lists, GenSeqs) esta ordenado
  List(1, 2, 3) shouldBe sorted

  // En el caso de un Iterator no se puede trabajar con contain: no tiene un Containg[Iterator[T]]
  //List(1, 2, 3).toIterator should contain (3) // Daria error
  // Es necesario convertirlo a un StreamS
  List(1, 2, 3).toIterator.toStream should contain (3)
}

class MatcherInspectorSuite extends FlatSpec with Matchers{
  /**
    * Testeo con Inspectores: En realidad los inspectors nos aclaran mas el codigo "syntactic sugar"
    */
  val xs = List(1, 2, 3, 4, 5)

  // Hago un should de cada elemento
  // Comprueba que todos sean mayores que 0
  all (xs) should be > 0 //Seria como hacer - forAll (xs) { x => x should be > 0 }

  // Comprueba si la mayoria de 2 elementos son mayores de 4
  atMost(2, xs) should be >= 4

  // Comprueba si al menos 3 elementos son menores de 5
  atLeast(3, xs) should be < 5

  // Comprueba si existen entre 2 y 3 elementos que sean mayores que 1 y menores que 5
  between(2, 3, xs) should (be > 1 and be < 5)

  // Comprueba si exactamente existen 2 elementos menores o iguales que 2
  exactly (2, xs) should be <= 2

  // Comprueba que todos sean menores que 10
  every (xs) should be < 10 //Igual que all

}

class MatcherOptionEitherSuite extends FlatSpec with Matchers{
  val some1 = Some(1)

  some1 should be (Some(1))
  some1 shouldBe defined
  some1 should not be empty

  val none = None
  none should be (None)

  val left1 = Left(1)
  left1 should be ('left)
  left1 should be (Left(1))

  val right1 = Right(1)
  right1 should be ('right)
  right1 should be (Right(1))

  import org.scalatest.EitherValues._
  val right3 = Right(3)
  right3.right.value should be < 7
}

class MatcherLogicalExpressionsSuite extends FlatSpec with Matchers{
  val result = 8
  result should (be > 0 and be < 10)

  val map = Map("hi" -> "HI", "hei" -> "HEI", "he" -> "HE")
  map should (contain key ("hi") or contain key ("ho"))
  // Sin parentesis no compilaria: map should contain key ("hi") or contain key ("ho")

  val map2 = Map("one" -> 1, "two" -> 2, "three" -> 3)
  map2 should (contain key ("two") and not contain value (7))

  val map3 = Map("ouch" -> 3)
  map3 should (not be (null) and contain key ("ouch"))

  val option = None
  option should (equal (Some(List(1, 2, 3))) or be (None))

  val string = "fum"
  string should (
    equal ("fee") or
      equal ("fie") or
      equal ("foe") or
      equal ("fum")
    )
}

class MatcherPatternSuite extends FlatSpec with Matchers{

  /**
    * Testear a traves de patrones
    */
  case class Name(first: String, middle: String, last: String)
  val name = Name("Jane", "Q", "Programmer")

  case class Record(name: Name, age: Int)
  val rec = Record( name = name, 38)

  import org.scalatest.Inside._

  // Utilizo el patron: Name(firstName, _, _)
  inside (rec) { case Record(name, age) =>
    inside (name) { case Name(firstName, _, _) =>
      firstName should startWith ("J")
    }
  }

  // O mas facil
  inside (name) { case Name(firstName, _, _) =>
    firstName should startWith ("J")
  }

  // Utilizo el patron: Name(_, "Q", _)
  name should matchPattern { case Name(_, "Q", _) => }
}


trait CustomMatchers {
  // Nos definimos un Matcher para tipos File
  class FileEndsWithExtensionMatcher(expectedExtension: String) extends Matcher[java.io.File] {
    // Pasamos directamente el tipo File
    def apply(left: java.io.File) = {
      val name = left.getName
      MatchResult(
        name.endsWith(expectedExtension),
        s"""File $name did not end with extension "$expectedExtension"""",
        s"""File $name ended with extension "$expectedExtension""""
      )
    }
  }

  def endWithExtension(expectedExtension: String) = new FileEndsWithExtensionMatcher(expectedExtension)
}

// Con esto conseguimos mas facilmente: solo con un import lo tenemos
object CustomMatchers extends CustomMatchers


class MatcherCustomSuite extends FlatSpec with Matchers{
  import CustomMatchers._

  // Podemos usar la funcion que hemos creado nosotro para tipos File junto con un should
  new File("prueba.txt") should endWithExtension("txt")
}


class MatcherComposeSuite extends FlatSpec with Matchers{
  /**
    * Componer funciones para crear una funcion nueva
    */
  // Operator > pasandole un Int
  val f = be > (_: Int)
  // Convierte a Int el String pasado por parametro
  val g = (_: String).toInt

  val beAsIntsGreaterThan = (f compose g) andThen (_ compose g) // En pseudocodigo: g(f(g(x))) ó g.f.g(x)

  "8" should beAsIntsGreaterThan ("7")

  // Pero no se puede meter un not:
  // "7" should not beAsIntsGreaterThan ("8")
}
