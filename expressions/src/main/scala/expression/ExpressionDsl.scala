package retronym.expression.dsl

import retronym.expression._

class ExpressionBuilder(e1: Expression) {
  def +~(e2: Expression) = new BinaryOp(e1, Plus(), e2)

  def -~(e2: Expression) = new BinaryOp(e1, Minus(), e2)

  def *~(e2: Expression) = new BinaryOp(e1, Multiply(), e2)

  def /~(e2: Expression) = new BinaryOp(e1, Div(), e2)
}

object ExpressionBuilder {
  implicit def ExpressionToExpressionBuilder[T <% Expression](e: T): ExpressionBuilder = new ExpressionBuilder(e)
}

