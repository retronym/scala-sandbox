package retronym.lessons

import _root_.org.specs.runner.ScalaTestSuite
import callbyname.CallByName
import collections.VectorAverage
import erasure.Erasure
import control.Control
import option.OptionalValues
import org.spex.Specification
import overload.Overload
import traits.UsingTraits

object Trail extends Specification {
 "Learning Trail" isSpecifiedBy(testing.specs.Sample, Control, UsingTraits, CallByName, Erasure, OptionalValues,
         VectorAverage, Overload)
}

