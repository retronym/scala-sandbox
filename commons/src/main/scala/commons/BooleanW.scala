package retronym.commons

object BooleanW {
  implicit def BooleanToBooleanW(b: Boolean) : BooleanW = new BooleanW(b)
}

class BooleanW(b : Boolean) {
  def iif[A](ifTrue : => A, ifFalse : => A) = {
    if(b) {ifTrue} else {ifFalse}
  }
  def iff[A](ifTrue : => A) = iif(Some(ifTrue), None)
}