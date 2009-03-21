package retronym.expression

import _root_.scalaz.OptionW
import _root_.retronym.commons._
import _root_.retronym.commons.BooleanW._
import _root_.retronym.commons.PartialFunctionW._

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

case class Operator(symbol: String) {
  override def toString = symbol
}

trait Commutative
trait Associative

trait HasIdentity[V] {
  def isIdentity(v: V): Boolean
}

case class Plus() extends Operator("+") with HasIdentity[Double] with Commutative with Associative {
  def isIdentity(v: Double) = v == 0
}

case class Minus() extends Operator("-") with HasIdentity[Double] {
  def isIdentity(v: Double) = v == 0
}

case class Multiply() extends Operator("x") with HasIdentity[Double] with Commutative with Associative {
  def isIdentity(v: Double) = v == 1
}

case class Div() extends Operator("/")

object Op {
  def + = Plus()

  def - = Minus()

  def x = Multiply()

  def / = Div()
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


class RichExpression(val e: Expression) {
  def simplify: Expression = {
    val choices = refactor.sort(_.length < _.length)
    choices.head
  }

  def refactorMultiPass = {
    var r1 = refactor.removeDuplicates
    var done = false;
    while (!done) {
      var r2 = (for (e <- r1; e1 <- e.refactor) yield e1).removeDuplicates
      done = r2.length == r1.length
      r1 = r2
    }
    r1
  }

  def refactor: List[Expression] = e match {
    case b: BinaryOp => refactorBinaryOp(b)
    case e => List(e)
  }

  val rules: List[PartialFunction[Expression, Expression]] = List(
    {case b@BinaryOp(_, o: Commutative, _) => b.flip},
    {case BinaryOp(a, o1: Associative, BinaryOp(b, o2: Associative, c)) if o1 == o2 => BinaryOp(BinaryOp(a, o1, b), o1, c)},
    {case BinaryOp(BinaryOp(a, o1: Associative, b), o2: Associative, c) if o1 == o2 => BinaryOp(a, o1, BinaryOp(b, o1, c))},
    // TODO Compiler warning because of type param Erasure. Maybe Expression should be Expression[DoubleNumericSystem] instead.
    {case BinaryOp(Constant(i), o: HasIdentity[Double], x) if o.isIdentity(i) => x},
    {case BinaryOp(x, o: HasIdentity[Double], Constant(i)) if o.isIdentity(i) => x},
    {case BinaryOp(x, Multiply(), Constant(0)) => Constant(0)},
    {case BinaryOp(Constant(0), Multiply(), x) => Constant(0)},
    {case BinaryOp(Constant(0), Div(), x) => Constant(0)},
    {case BinaryOp(a, Div(), b) if a == b => Constant(1)},
    {case BinaryOp(x, Minus(), y) if x == y => Constant(0)},
    {case BinaryOp(BinaryOp(x, op, y), Div(), z) if op == Minus() || op == Plus() => BinaryOp(new BinaryOp(x, Div(), z), op, new BinaryOp(y, Div(), z))},
    {case BinaryOp(BinaryOp(a, Div(), b), op, BinaryOp(c, Div(), d)) if b == d => BinaryOp(BinaryOp(a, op, c), Div(), b)},
    {case b => b})

  private def refactorBinaryOp(e: BinaryOp): List[Expression] = {
    val es = e match {
      case BinaryOp(l, o, r) => for (l1 <- l.refactor; r1 <- r.refactor) yield BinaryOp(l1, o, r1)
    }

    import PartialFunctionW._
    for (e1 <- es; r <- rules; e2 <- r.toFunction1(e1)) yield e2
  }
}
