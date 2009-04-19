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
import org.specs.runner.ScalaTestSuite

class expressionSpecAdapter extends ScalaTestSuite(expressionSpec)

object expressionSpec extends Specification {
  "describe" in {
    val op = BinaryOp(Constant(1), Plus, Constant(0))
    op.describe must be_==("1.0 + 0.0")
  }

  "implicit conversion" in {
    val expr = BinaryOp(1, Plus, "x")
    expr must be_==(BinaryOp(Constant(1), Plus, Variable("x")))
  }

  "arithmetic simplified" in {
    (0 +~ "x").simplify must expr_==("x")
    ("x" +~ 0).simplify must expr_==("x")
    (0 +~ "x").simplify must expr_==("x")
    ("x" *~ 1).simplify must expr_==("x")
    (1 *~ "x").simplify must expr_==("x")
    ((0 +~ 0) +~ "x").simplify must expr_==("x")
  }

  "complex expression is fully refactored" in {
    val orig = ("a" +~ "b") -~ ("b" +~ "a");
    orig.refactor must contain(("b" +~ "a") -~ ("b" +~ "a"))
    orig.refactor must contain(("b" +~ "a") -~ ("a" +~ "b"))
    orig.refactor must contain(Constant(0))
  }
  
  "associative rule" in {
    val e1 = ("a" +~ "b") +~ "c";
    val e2 = "a" +~ ("b" +~ "c")

    e1.refactor must containAll(List[Expression](e1, e2))
    e1.refactor must contain(e1)
    e2.refactor must contain(e1)
  }

  "divide" in {
    val e1 = ("a" +~ "b") /~ "b";
    val value = ("a" /~ "b") +~ 1
    e1.refactor must contain(value)
  }
}

