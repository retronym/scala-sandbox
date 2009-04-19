package retronym.finance

import java.math.BigDecimal
import java.util.Date
import scalaz.control.Monad

case class PayoffStyle()
case object Put extends PayoffStyle
case object Call extends PayoffStyle

case class Level()

case class AbsoluteLevel(value: BigDecimal)
case class PercentLevel(value: BigDecimal)

trait Underlying {
  def getId: String
}

trait BasketStyle;
case object WorstOf extends BasketStyle;
case object BestOf extends BasketStyle;
case object Average extends BasketStyle;
case object Single extends BasketStyle;

case class OptionCalendar(forwardStart: Option[Date])



case class OptionPayoff(
                       strike: Level,
                       style: PayoffStyle,
                       basketStyle: BasketStyle
                       )

case class OptionInstrument(payoff: OptionPayoff, optionCalendar: OptionCalendar)

trait Instrument

class BasketComponent(val instrument: Instrument, val q: BigDecimal)

class Basket(components: List[BasketComponent]) extends Instrument {
}

trait NamedInstrument {
  def name;
}

