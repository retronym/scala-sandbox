package retronym.expression

import _root_.scalaz.OptionW
import _root_.retronym.commons._

trait Expression {
  def describe: String

  def length: Int = 1

  def refactor = new RichExpression(this).refactor

  def refactorMultiPass = new RichExpression(this).refactorMultiPass

  def simplify = new RichExpression(this).simplify
}

case class Constant(value: Double) extends Expression {
  def describe = {
    value.toString;
  }
}

case class Variable(name: String) extends Expression {
  def describe = {
    name.toString
  }
}

object Expression {
  implicit def fromDouble(value: Double) = Constant(value)

  implicit def fromInt(value: Int): Constant = {
    Constant(value)
  }

  implicit def fromString(value: String) = Variable(value)
}

case class BinaryOp(left: Expression, op: Operator, right: Expression) extends Expression {
  def describe = {
    left.describe + " " + op.toString + " " + right.describe
  }

  def flip = BinaryOp(right, op, left)

  override def length = left.length + right.length
}