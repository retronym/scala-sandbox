package retronmym.expression.parse

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
import retronym.expression.expr_==
import retronym.expression.parse.{ ExpressionExternalDSL}
import scala.util.parsing.combinator.syntactical._

import _root_.retronym.expression.dsl.ExpressionBuilder._
import _root_.retronym.expression.RichExpression._
import ExpressionExternalDSL.parse

class ExpressionExternalDSLTest extends TestCase("expression external dsl") with SpecsMatchers {
  def testParseCostant() : Unit = {
    parse("0") must expr_==(0)
    parse("x") must expr_==("x")
    parse("(0 + 1)") must expr_==(0 +~ 1)
    parse("(x + 1)") must expr_==("x" +~ 1)
    parse("(x + (x - 1))") must expr_==("x" +~ ("x" -~ 1))
  }
}