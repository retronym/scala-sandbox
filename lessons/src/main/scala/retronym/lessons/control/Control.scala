package retronym.lessons.control

import _root_.org.specs.runner.ScalaTestSuite
import _root_.org.specs.Specification
import _root_.org.specs.runner._
import _root_.org.specs.matcher._
import _root_.org.specs.mock._
import _root_.org.specs.specification._
import _root_.org.specs._
import _root_.org.specs.io._
import _root_.org.specs.collection._
import _root_.org.specs.util._
import _root_.org.specs.xml._

object Control extends Specification {
  "Basic if/else" should {
    "basic if statement" in {
      val a = 1
      var b = "";
      if (a == 1) {
        b = "1"
      }
      b must be_==("1")
    }

    "if 'statement' is actually an expression" in {
      val a = 1
      val b = if (a == 1) {
        "1"
      } else {
        "other"
      }
      b must be_==("1")
    }

    "only useful if you have an else, though." in {
      var b = if (1 == 0) {
        "1"
      }
      // http://en.wikipedia.org/wiki/Unit_type
      val unit: Unit = ()
      b must be_==(unit)
    }
  }
}
