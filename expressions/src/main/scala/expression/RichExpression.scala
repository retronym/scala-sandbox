package retronym.expression

import _root_.retronym.commons.BooleanW._
import _root_.retronym.commons.PartialFunctionW._

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

    for (e1 <- es; r <- rules; e2 <- r.toFunction1(e1)) yield e2
  }
}
