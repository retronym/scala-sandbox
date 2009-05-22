package retronym.lessons.erasure

import org.spex.Specification

object Erasure extends Specification {
  "Erasure" should {
    "Type parameter 'T' is erased, and the instance of check always returns true" in {
      def instanceOf[T](e: AnyRef) = e.isInstanceOf[T]
      instanceOf[Int]("not an integer") must be_==(true)
    }
  }

  "Manifests" should {
    "Compiler passed manifest implicitly" in {
      def instanceOf2[T] (e: AnyRef)(implicit m: scala.reflect.Manifest[T]) = {
        e.getClass == m.erasure

      }
      instanceOf2[Int]("foo") must beFalse
      instanceOf2[String]("foo") must beTrue
    }

    "Manifest contains the full generic type" in {
      def fullType[T](implicit m: scala.reflect.Manifest[T]) = {
        (m.erasure, m.toString)
      }
      val (erasure, manifestString) = fullType[List[Int]]
      manifestString must be_==("scala.List[int]")
    }
  }
}
