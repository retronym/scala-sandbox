package retronym.expression

import dsl.ExpressionBuilder._
import dsl._

import junit.framework.{Test, Assert, TestCase}
import junit.framework.Assert._
import _root_.org.specs.runner._
import _root_.org.specs.matcher._
import _root_.org.specs.mock._
import _root_.org.specs.specification._
import _root_.org.specs._
import _root_.org.specs.io._
import _root_.org.specs.collection._
import _root_.org.specs.util._
import _root_.org.specs.xml._

// TODO Figure out how to use Specs directly with IntelliJ, rather than JUnit.
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