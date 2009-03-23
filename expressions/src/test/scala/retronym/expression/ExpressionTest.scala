package retronym.expression

import dsl.ExpressionBuilder._
import dsl._
import RichExpression._

import _root_.junit.framework.Assert._
import _root_.org.specs.runner._
import _root_.org.specs.matcher._
import _root_.org.specs.mock._
import _root_.org.specs.specification._
import _root_.org.specs._
import _root_.org.specs.io._
import _root_.org.specs.collection._
import _root_.org.specs.util._
import _root_.org.specs.xml._
import _root_.junit.framework.{TestResult, Test, Assert, TestCase}
import _root_.org.junit.runner.notification.RunNotifier
import _root_.org.junit.runner.{Runner, RunWith, Description}
import _root_.org.junit.runners.Suite

// TODO This approach is not satisfactory from the IntelliJ JUnit Runner as the individual test are not named
//      Look into changing the Specs <-> JUnit4 Adapter.
//@RunWith(classOf[JUnitSuiteRunner])
//class mySpecJunit4 extends JUnit4(mySpec)
//
//object mySpec extends Specification {
//  "'hello world' has 11 characters" in {
//    println("hello!")
//     "hello world".size must_== 11
//  } 
//  "'hello world' matches 'h.* w.*'" in {
//     "hello world" must beMatching("h.* w.*")
//  }
//}

class ExpressionTest extends TestCase("expression") with SpecsMatchers {
  def testExpressionOk : Unit = {
    val op = BinaryOp(Constant(1), Plus(), Constant(0))
    op.describe must be_==("1.0 + 0.0")
  }

  def testImplicits : Unit = {
    val expr = BinaryOp(1, Plus(), "x")
    expr must be_==(BinaryOp(Constant(1), Plus(), Variable("x")))
  }

  def testSimplifiy : Unit = {
    (0 +~ "x").simplify must expr_==("x")
    ("x" +~ 0).simplify must expr_==("x")
    (0 +~ "x").simplify must expr_==("x")
    ("x" *~ 1).simplify must expr_==("x")
    (1 *~ "x").simplify must expr_==("x")
  }

  def testSimplifiy1 : Unit = {
    ((0 +~ 0) +~ "x").simplify must expr_==("x")
  }

  def testRefactorComplex : Unit = {
    val orig = ("a" +~ "b") -~ ("b" +~ "a");
    orig.refactor must contain(("b" +~ "a") -~ ("b" +~ "a"))
    orig.refactor must contain(("b" +~ "a") -~ ("a" +~ "b"))
    orig.refactor must contain(Constant(0))
  }
  
  def testRefactorAssociative : Unit = {
    val e1 = ("a" +~ "b") +~ "c";
    val e2 = "a" +~ ("b" +~ "c")

    e1.refactor must contain(e2)
    e1.refactor must contain(e1)
    e2.refactor must contain(e1)
  }

  def testDivide : Unit = {
    val e1 = ("a" +~ "b") /~ "b";
    val value = ("a" /~ "b") +~ 1
    e1.refactorMultiPass must contain(value)
  }
}

case class expr_==(e: Expression) extends Matcher[Expression] {
  override def apply(t: =>Expression) = {
    (e == t, "matched", "expected: " + e.describe)
  }
}