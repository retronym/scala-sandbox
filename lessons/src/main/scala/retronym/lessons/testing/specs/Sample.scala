package retronym.lessons.testing.specs

import _root_.org.spex.Specification


object Sample extends Specification {
  "Basic Examples" should {
    "this example will fail" in {
      1 must be_==(2)
    }
    "this example will pass" in {
      1 must be_==(1)
    }
  }
  
}
