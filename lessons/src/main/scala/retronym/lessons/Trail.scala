package retronym.lessons

import _root_.org.specs.runner.ScalaTestSuite
import callbyname.CallByName
import erasure.Erasure
import control.Control
import option.OptionalValues
import org.spex.Specification
import traits.UsingTraits

object Trail extends Specification {
 "Learning Trail" isSpecifiedBy(testing.specs.Sample, Control, UsingTraits, CallByName, Erasure, OptionalValues)
}

