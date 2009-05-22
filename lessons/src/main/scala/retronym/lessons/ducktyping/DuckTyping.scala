package retronym.lessons.ducktyping

import _root_.org.spex.Specification

object DuckTyping extends Specification {
  class A {
    def close() = "A.close"
  }
  class B {
    def close() = "B.close"
  }

  "DuckTyping" should {
    "closeIt() accepts anything that has a close method that returns a string" in {
      def closeIt(c: {def close(): String}) = {
        c.close
      }

      closeIt(new A) must be_==("A.close")
      closeIt(new B) must be_==("B.close")
    }
    
    "Same as above, but declare a type Closable to give a name to this structural type" in {
      type Closable = {def close(): String}

      def closeIt(c: Closable) = {
        c.close
      }

      closeIt(new A) must be_==("A.close")
      closeIt(new B) must be_==("B.close")
    }
  }
}
