package retronym.expression

import dsl.ExpressionBuilder._
import dsl._

import junit.framework.{Test, Assert, TestCase}
import junit.framework.Assert._
import Op._
import _root_.org.specs.runner._
import _root_.org.specs.matcher._
import _root_.org.specs.mock._
import _root_.org.specs.specification._
import _root_.org.specs._
import _root_.org.specs.io._
import _root_.org.specs.collection._
import _root_.org.specs.util._
import _root_.org.specs.xml._

class ExpressionTest extends TestCase("expression") with SpecsMatchers {
  def testExpressionOk : Unit = {
    val op = BinaryOp(Constant(1), Op.+, Constant(0))
    op.describe must be_==("1.0 + 0.0")
  }

  def testImplicits : Unit = {
    val expr = BinaryOp(1, Op.+, "x")
    expr must be_==(BinaryOp(Constant(1), Op.+, Variable("x")))
  }

  def testSimplifiy : Unit = {
    checkSimplify("x", 0 +~ "x")
    checkSimplify("x", "x" +~ 0)
    checkSimplify("x", "x" *~ 1)
    checkSimplify("x", 1 *~ "x")
  }

  def testSimplifiy2 : Unit = {
    checkSimplify("x", (0 +~ 0) +~ "x")
  }

  def testSimplifiyComplex : Unit = {
    checkSimplify("x", (0 +~ 0) +~ "x")
  }

  def testSimplifiy3 : Unit = {
    // 1. (a + b) - (b + a)
    // 1.1 (b + a) - (b + a)
    //  1.1.1 0
    // 1.2 (a + b) - (a + b)
    // 1.2.1 0
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
    e1.refactor2Pass must contain(value)
  }

  def checkSimplify(expected: Expression, original: Expression) {
    original.simplify must be_==(expected)
  }

}
