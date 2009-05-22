package retronym.lessons.option

import _root_.org.spex.Specification


object OptionalValues extends Specification {
  "Java Style" should {
    def add(a: java.lang.Integer, b: java.lang.Integer) : java.lang.Integer = {
      if (a == null || b == null) return null
      return a.intValue + b.intValue
    }

    "First is null" in {
      add(null, 1) must beNull
    }
    "Second is null" in {
      add(1, null) must beNull
    }
    "Both are null" in {
      add(null, null) must beNull
    }
    "Both are non-null" in {
      add(1, 2) must be_==(3)
    }
  }
  "Scala Style" should {
    def add(a: Int, b: Int) = a + b
    def addOptionalInputs(a: Option[Int], b: Option[Int]) = {
      for(a <- a; b <- b) yield add(a, b)
    }
    "First is null" in {
      addOptionalInputs(None, Some(1)) must beNone
    }
    "Second is null" in {
      addOptionalInputs(Some(1), None) must beNone
    }
    "Both are null" in {
      addOptionalInputs(None, None) must beNone
    }
    "Both are non-null" in {
      addOptionalInputs(Some(1), Some(2)) must beSome[Int].which(_ == 3)
    }
  }
}
