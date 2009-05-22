

package retronym.expression

case class Operator(symbol: String) {
  override def toString = symbol
}

trait Commutative
trait Associative

trait HasIdentity[V] {
  def isIdentity(v: V): Boolean
}

case object Plus extends Operator("+") with HasIdentity[Double] with Commutative with Associative {
  def isIdentity(v: Double) = v == 0
}

case object Minus extends Operator("-") with HasIdentity[Double] {
  def isIdentity(v: Double) = v == 0
}

case object Multiply extends Operator("x") with HasIdentity[Double] with Commutative with Associative {
  def isIdentity(v: Double) = v == 1
}

case object Div extends Operator("/")
