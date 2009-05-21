package retronym.lessons.overload

import _root_.org.spex.Specification


object Overload extends Specification {
  "Overload" should {
    class M {
      final def m = "m"
    }

    case class Wrap1(a: String)
    case class Wrap2(a: String)

    class IllegalOverride extends M {
      // def m() = "m"    // can't overload based on no-param list and empty param list.
      // def m :Int = 1   // can't overload based on return type
      // var m = 1        // one namespace for methods, vals, vars, and objects to support uniform access principle.
      // val m = 1
      // object m
    }

    "override with implicit param" in {
      class ImplicitParam

      class ImplicitOveride extends M {
        def m(implicit a: ImplicitParam) = "m(implicit a: Any)"
      }

      {
        implicit val a = new ImplicitParam
        // error: errorneous reference to overloaded definition, most specific 'm', alternative 'm(implicit a)'
        // new ImplicitOveride().m must be_==("m")
        // new ImplicitOveride().m must be_==("m(implicit a: Any)")
        ()
      }

      new ImplicitOveride().m(new ImplicitParam) must be_==("m(implicit a: Any)")
    }

    "override by type param" in {

      class TypeParamaterAndReturnTypeOveride extends M {
        def m[T] = Wrap1("m[T]")
        // def m[T, Y] = Wrap1("m[T, Y]")   // error: double definition, same type after erasure
        def m[T, Y] = Wrap2("m[T, Y]")
      }

      new M().m must be_==("m")
      // new TypeParamaterAndReturnTypeOveride().m must be_==("m") // error: ambiguous reference to overloaded definition
      val x = new TypeParamaterAndReturnTypeOveride()
      x.m[Any] must be_==(Wrap1("m[T]"))
      x.m[Nothing] must be_==(Wrap1("m[T]"))
      x.m[String, Int] must be_==(Wrap2("m[T, Y]"))
    }


    class ParamListOverride extends M {
      def m(a: Int) = "m(a: Int)"

      def m(a: String) = "m(a: String)"

      def m(a: List[Int]) = "m(a: List[Int])"
      // def m(a: List[String]) = 1   // double definition... have same type after erasure.
      def m(a: Any, b: Any) = "m(a: Any, b: Any)"
      // def m(a: Any)(b: Any) = "m(a: Any)(b: Any)" // double definition
       def m(a: Any)(b: Any) = Wrap1("m(a: Any)(b: Any)") // double definition
    }


    "classes, types, and traits in a separate namespace" in {
      class NestedClass extends M {
        class M
      }

      class NestedTrait extends M {
        trait M
      }

      class NestedType extends M {
        type M = Any
      }
            
      new NestedClass().m must be_==("m")
      new NestedTrait().m must be_==("m")
      new NestedType().m must be_==("m")
    }
  }
}
