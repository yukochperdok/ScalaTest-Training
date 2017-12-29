package com.stratio.examplesTest

import java.io.{File, FileWriter}

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FlatSpec, fixture}

import scala.collection.mutable.ListBuffer

/**
  * Es muy recomendable la reutilizacion de codigo: variables y metodos. Opciones:
  *   1. fixture-methods: refactorizacion de metodos (o variables) generales
  *   2. fixture-context objects: traits generales
  *   3. loan-fixture methods: metodos reutilizables utilizando la funcion test como parametro
  *   4. Utilizar BeforeAndAfter or BeforeAndAfterAll
  */

class FixtureMethodsSpec extends FlatSpec {

  def fixture =
    new {
      val builder = new StringBuilder("ScalaTest is ")
      val buffer = new ListBuffer[String]
    }

  "Testing" should "be easy" in {
    val f = fixture
    f.builder.append("easy!")
    assert(f.builder.toString === "ScalaTest is easy!")
    assert(f.buffer.isEmpty)
    f.buffer += "sweet"
  }

  it should "be fun" in {
    val f = fixture
    f.builder.append("fun!")
    assert(f.builder.toString === "ScalaTest is fun!")
    assert(f.buffer.isEmpty)
  }
}


class FixtureObjectsSpec extends FlatSpec {

  override def withFixture(test: NoArgTest) = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }

  trait Builder {
    val builder = new StringBuilder("ScalaTest is ")
  }

  trait Buffer {
    val buffer = ListBuffer("ScalaTest", "is")
  }

  // Si solo necesito testear StringBuilder, utilizo Builder fixture
  "Testing" should "be productive" in new Builder {
    builder.append("productive!")
    assert(builder.toString === "ScalaTest is productive!")
  }

  // Si solo necesito testear ListBuffer[String], utilizo Buffer fixture
  it should "be readable" in new Buffer {
    buffer += ("readable!")
    assert(buffer === List("ScalaTest", "is", "readable!"))
  }

  // Si necesito ambas, utilizo ambas
  it should "be clear and concise" in new Builder with Buffer {
    builder.append("clear!")
    buffer += ("concise!")
    assert(builder.toString === "ScalaTest is clear!")
    assert(buffer === List("ScalaTest", "is", "concise!"))
  }
}

class LoanFixtureSpec extends FlatSpec {

  def withFile(testCode: (File, FileWriter) => Any) {
    val file = File.createTempFile("withFile", ".txt") // creo el fixture
    val writer = new FileWriter(file)
    try {
      writer.write("ScalaTest is ") // inicializo el fixture
      testCode(file, writer) // ejecuto el test pasado por parametro (teniendo acceso a file y writer)
    }
    finally writer.close() // limpio el fixture
  }

  // Utilizo el fixture withFile pasandole una funcion por parametro
  "Testing" should "be productive" in withFile { (file, writer) =>
    writer.write("productive!")
    writer.flush()
    assert(file.length === 24)
  }

  it should "be clear" in withFile { (file, writer) =>
    writer.write("clear!")
    writer.flush()
    assert(file.length === 19)
  }
}

class WithFixtureSpec extends fixture.FlatSpec {

  case class FixtureParam(file: File, writer: FileWriter)

  //Sobrescribo el metodo withFixture
  override def withFixture(test: OneArgTest) = {
    val file = File.createTempFile("withFixture", ".txt") // creo el fixture
    val writer = new FileWriter(file)
    val theFixture = FixtureParam(file, writer)

    try {
      writer.write("ScalaTest is ") // inicializo the fixture
      withFixture(test.toNoArgTest(theFixture)) // llamo al padre para ejecutar test
    }
    finally writer.close() // limpio el fixture
  }

  // No hace falta indicar withFixture, se le llama pasandole el fixture entero
  "Testing" should "be productive" in { f =>
    f.writer.write("productive!")
    f.writer.flush()
    assert(f.file.length === 24)
  }

  it should "be clear" in { f =>
    f.writer.write("clear!")
    f.writer.flush()
    assert(f.file.length === 19)
  }

}


/**
  * Tambien se pueden utilizar los SuiteMixin (BeforeAndAfter, BeforeAndAfterEach or BeforeAndAfterAll)
  * Si te interesa realizar operaciones o tratamientos fuera de los propios test (antes o despues)
  */
class BeforeAndAfterSpec extends FlatSpec with BeforeAndAfter {

  // Compartes variables, no puedes encapsularlas ni en el before, ni en el after
  // por lo tanto tienes que declararlas como var para poder reasignarlas
  var file: File = _
  var writer: FileWriter = _

  before {
    file = File.createTempFile("beforeAndAfter", ".txt")
    writer = new FileWriter(file)
    writer.write("ScalaTest is ")
  }

  after {
    writer.close()
  }

  "Testing" should "be productive" in {
    writer.write("productive!")
    writer.flush()
    assert(file.length === 24)
  }

  it should "be clear" in {
    writer.write("clear!")
    writer.flush()
    assert(file.length === 19)
  }
}

class BeforeAndAfterAllSpec extends FlatSpec with BeforeAndAfterAll {

  var file: File = _
  var writer: FileWriter = _


  override def beforeAll() {
    file = File.createTempFile("beforeAndAfterAll", ".txt")
    writer = new FileWriter(file)
    writer.write("ScalaTest is ")
  }


  // Delete the temp file
  override def afterAll() {
    writer.close()
  }

  "Testing" should "be productive" in {
    writer.write("productive!")
    writer.flush()
    assert(file.length === 24)
  }

  it should "be clear" in {
    writer.write("clear!")
    writer.flush()
    assert(file.length === 30)
  }
}


