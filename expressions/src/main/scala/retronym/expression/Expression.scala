package retronym.expression

import _root_.scalaz.OptionW
import _root_.retronym.commons._

trait Expression {
  def describe: String

  def length: Int = 1
}

sealed abstract class BaseExpression extends Expression

case class Constant(value: Double) extends BaseExpression {
  def describe = {
    value.toString;
  }
}

case class Variable(name: String) extends BaseExpression {
  def describe = {
    name.toString
  }
}

case class BinaryOp(left: Expression, op: Operator, right: Expression) extends BaseExpression {
  def describe = left.describe + " " + op.toString + " " + right.describe

  def flip = BinaryOp(right, op, left)

  override def length = left.length + right.length
}

object Expression {
  implicit def fromDouble(value: Double) = Constant(value)

  implicit def fromInt(value: Int): Constant = Constant(value)

  implicit def fromString(value: String) = Variable(value)
}