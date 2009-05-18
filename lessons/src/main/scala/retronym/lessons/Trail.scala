package retronym.lessons

import _root_.org.specs.runner.ScalaTestSuite
import erasure.Erasure
import control.Control
import option.OptionalValues
import org.spex.Specification

object Trail extends Specification {
 "Learning Trail" isSpecifiedBy(testing.specs.Sample, Control, Erasure, OptionalValues)
}

