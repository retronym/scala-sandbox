package retronym.lessons.salutation

// Obligatory Hello World!
object HelloWorldApp {
  // No static methods in Scala. main() is a method on the singleton object that I have
  // called HelloWorldApp.
  //
  def main(args: Array[String]) = {
    hello4(args)
  }

  // This is identical to main. scala.Predef is automatically imported, along with java.lang.
  def hello1(args: Array[String]) = println("Hello, world!")

  // This is identical to main. scala.Predef is automatically imported, along with java.lang.
  def hello2(args: Array[String]) = {
    scala.Predef.println("Hello, world!")
  }

  def hello3(args: Array[String]) = {
    import scala.Predef.{println => printline}
    printline("Hello World")
    println
  }

  // Object Oriented!
  def hello4(args: Array[String]) = {
    new Salutation("Hello").greet("World")
  }
}

class Salutation(word: String) {
  def greet(someone: String) = println(word + ", " + someone)
}