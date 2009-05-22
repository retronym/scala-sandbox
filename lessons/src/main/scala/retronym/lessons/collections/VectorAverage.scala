package retronym.lessons.collections

import _root_.org.spex.Specification
import org.specs.matcher.Matcher
import scala.Stream
import scalaz.Equal


object VectorAverage extends Specification {
  def zipAll[A](streams: List[Stream[A]]): Stream[List[A]] = {
    if (streams.exists(_.isEmpty)) Stream.empty
    else Stream.cons(streams.map(_.head), zipAll(streams.map(_.tail)))
  }

  def zipAllWith[A, B](streams: List[Stream[A]], f: List[A] => B): Stream[B] = {
    zipAll(streams).map(f)
  }

  def averageList(vals: List[Double]) = {
    vals.reduceLeft(_ + _) / vals.length
  }

  def average(vectors: List[List[Double]]): List[Double] = {
    val maxLength = vectors.map((_.length)).reduceLeft(Math.max _)
    val streams = vectors.map(_.toStream.append(Stream.const(0.0)))
    zipAllWith(streams, averageList _).take(maxLength).force
  }

  "VectorAverage" should {
    "avergage" in {
      println(classOf[BigInt].getName, classOf[BigInt].getProtectionDomain.getCodeSource)
      val input = List(List(1.0, 1.5, 2.0), List(3.0, 1.5))
      implicit val eqMilli = FixedEqual.EqualApproxDouble(0.001)
      import FixedEqual.EqualSeq
      average(input) must be_Equal(List(2.0, 1.5, 1.0))
    }
  }
}

case class be_Equal[T](expected: T)(implicit eq: Equal[_ >: T]) extends Matcher[T] {
  override def apply(actual: => T) = {
    (eq.equal(expected, actual), "matched", "expected: " + expected)
  }
}

object FixedEqual {
  import scalaz.Equal.equal

  implicit def EqualSeq[A](implicit e: Equal[A]): Equal[Seq[A]] = equal[Seq[A]]((a1, a2) => a1.equalsWith(a2)(e.equal _))

  def EqualApproxDouble(tolerance: Double): Equal[Double] = equal[Double]((a1, a2) => Math.abs(a1 - a2) <= tolerance)
}

