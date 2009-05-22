package retronym.lessons.testing.specs

import _root_.org.spex.Specification

object Sample extends Specification {
  "Basic Examples" should {
    "this example will pass" in {
      1 must be_==(1)
    }
  }

  val spec = declare("Basic Examples without shorthand")
  spec.forExample("this will pass").in({
    1 must be_==(1)
  })
}
