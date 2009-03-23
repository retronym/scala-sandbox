package retronym.commons

import BooleanW._

object PartialFunctionW {
  implicit def PartialFunctionToPartialFunctionW[A, B](pf: PartialFunction[A, B]) : PartialFunctionW[A, B] = {
    new PartialFunctionW(pf)
  }
}

class PartialFunctionW[-A, +B](pf: PartialFunction[A, B]) {
  def toFunction1 : Function1[A, Option[B]] = {
    (v1: A) => pf.isDefinedAt(v1).iff(pf(v1))
  }
}
