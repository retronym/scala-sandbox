package retronym.lessons.valsvars

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

class ValsVarsAdapter extends ScalaTestSuite(ValsVars)

object ValsVars extends Specification {
  "var" should {
    "can be reassigned" in {
      var a = 1
      var b = 2
      b = a
      b must be_== (a)
    }
  }

  "val" should {
    "cannot re-assign" in {
      Compiler
    }
  }
}
