package retronym.lessons.callbyname

import _root_.org.spex.Specification


object CallByName extends Specification {
  "CallByName" should {
    trait StringSource {
      def make: String
    }

    def repeat(count: Int, s: => String) = {
      (0 until count).foldLeft("")((b: String, _: Any) => b + s)
    }

    "call by name evaluate the parameter lazily and repeatedly" in {
      val mockedStringSource = mock[StringSource]
      mockedStringSource.make returns "first" thenReturns "second"
      val repeated = repeat(2, mockedStringSource.make)
      repeated must be_==("firstsecond")
      mockedStringSource.make was called.twice
    }

    def repeatNormal(count: Int, s: String) = {
      (0 until count).foldLeft("")((b: String, _: Any) => b + s)
    }

    "call by value evaluates the parameter eaguerly and once" in {
      val mockedStringSource = mock[StringSource]
      mockedStringSource.make returns "first"
      val repeated = repeatNormal(2, mockedStringSource.make)
      repeated must be_==("firstfirst")
      mockedStringSource.make was called.once
    }
  }
}
