package retronym.lessons.callbyname

import _root_.org.spex.Specification

/**
 * 4.6.1 The type of a value parameter may be prefixed by =>, e.g. x : => T . The type of
 * such a parameter is then the parameterless method type => T . This indicates that
 * the corresponding argument is not evaluated at the point of function application,
 * but instead is evaluated at each use within the function. That is, the argument is
 * evaluated using call-by-name.<br/>
 * <hr/>
 * Practical when: <br/>
 * <ul>
 *   <li>in a logging API, so you don't create the message unless the threshold is set.</li>
 *   <li>to implement a new control structure, such as a do-while loop</li>
 *   <li>DSL creation</li>
 * </ul>
 * Drawbacks:<br/>
 * <ul>
 *   <li>When reading client code, a programmer expects method arguments to be evaluated eagerly. Better tool support may mitigate this.</li>
 * </ul>
 */
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
