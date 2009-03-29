package retronym.expression.parse

import _root_.scala.util.parsing.combinator.syntactical.{StandardTokenParsers, StdTokenParsers}
import retronym.expression.Expression

import scala.util.parsing.combinator.lexical.StdLexical

object ExpressionExternalDSL extends StandardTokenParsers {
//  val lexical = new StdLexical

  def parse(s: String): Expression = {
    val result: ParseResult[Expression] = phrase(expr)(new lexical.Scanner(s))
    result.getOrElse(error(result.toString))
  }

  lexical.delimiters ++= List("(", ")", "+", "-", "*", "/")
  
  def expr: Parser[Expression] = binary_operation | atom

  def atom: Parser[Expression] = variable | constant

  def binary_operation: Parser[Expression] = "(" ~> expr  ~ operator ~ expr <~ ")" ^^ {case l ~ o ~ r => BinaryOp(l, o, r)}

  def variable: Parser[Expression] = ident ^^ {s => Variable(s)}

  def constant: Parser[Constant] = numericLit ^^ {s => Constant(s.toDouble)}

  def operator: Parser[Operator] = plus | minus | mul | div

  def plus: Parser[Operator] = "+" ^^ {_ => Plus()}

  def minus: Parser[Operator] = "-" ^^ {_ => Minus()}

  def mul: Parser[Operator] = "*" ^^ {_ => Multiply()}

  def div: Parser[Operator] = "/" ^^ {_ => Div()}
}