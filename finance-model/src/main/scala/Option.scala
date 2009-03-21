import java.math.BigDecimal
import java.util.Date
import scalaz.OptionW

case class PayoffStyle
case class Put extends PayoffStyle
case class Call extends PayoffStyle

case class Level

case class AbsoluteLevel(value: BigDecimal)
case class PercentLevel(value: BigDecimal)

trait Underlying {
  def getId: String
}

trait BasketStyle;
case class WorstOf extends BasketStyle;
case class BestOf extends BasketStyle;
case class Average extends BasketStyle;
case class Single extends BasketStyle;

case class OptionCalendar(forwardStart: OptionW[Date] )

case class OptionPayoff(
                       strike: Level,
                       style: PayoffStyle,
                       basketStyle: BasketStyle
                       )

case class Option(payoff : OptionPayoff,
                 optionCalendar : OptionCalendar)

