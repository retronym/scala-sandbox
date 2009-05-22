package retronym.lessons.annotations

import _root_.org.specs.runner.ScalaTestSuite
import _root_.org.specs.Specification
import _root_.org.specs.runner._
import _root_.org.specs.matcher._
import _root_.org.specs.mock._
import _root_.org.specs.specification._
import _root_.org.specs._
import _root_.org.specs.io._
import _root_.org.specs.collection._
import _root_.org.specs.util._
import _root_.org.specs.xml._
import java.lang.reflect.Field
import java.lang.reflect.Method

object Annotation extends Specification {
  class MyClass {
    @Deprecated
    var myVal = ""
  }
  // https://lampsvn.epfl.ch/trac/scala/ticket/1846
  "An annotated var" should {
    "have an annotated field" in {
      val field = classOf[MyClass].getDeclaredField("myVal")
      val annotation: Deprecated = field.getAnnotation(classOf[Deprecated])
      annotation mustNot beNull
    }
    "have an annotated accessor" in {
      val method = classOf[MyClass].getMethod("myVal")
      val annotation: Deprecated = method.getAnnotation(classOf[Deprecated])
      annotation mustNot beNull
    }
    "have an annotated mutator" in {
      val found = classOf[MyClass].getMethods().find(_.getName == "myVal_$eq")
      found must beSome[Method].which((_: Method).getAnnotation(classOf[Deprecated]) mustNot beNull)      
    }
  }
}
