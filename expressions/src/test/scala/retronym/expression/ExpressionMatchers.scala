package retronym.expression

import _root_.org.specs.matcher.Matcher

case class expr_==(e: Expression) extends Matcher[Expression] {
  override def apply(t: =>Expression) = {
    (e == t, "matched", "expected: " + e.describe)
  }
}