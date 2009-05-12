package retronym.finance.termsheet


import java.util.Date

class RichText
trait PayoffChart

class UnderlyingRow (
  val name: String
)

class UnderlyingSection(
  val introduction: Option[String],
  val underlyings: List[UnderlyingRow],
  val provisions: List[String]
)
{

}

class CouponSection(

)

class GeneralInfo(
  val isin: String,
  val coupons: CouponSection
)



class RedemptionScenarios

class Definition(
  val term: String,
  val definition: RichText)

class Redemption(
  val introduction: String,
  val scenarios: RedemptionScenarios,
  val definitions: List[Definition]
)

trait DateDefinition

class SingleDateDefinition(val name: String, val date: Date, val provision: Option[RichText])
class ScheduleDefinition(val scheduleName: String, val date: Date, val provision: Option[RichText])

class DatesSection(
  val dateDefinitions: List[DateDefinition]
        )

class Termsheet(
  val title: String,
  val description: RichText,
  val marketExpectation: String,
  val payoff: PayoffChart,
  val underlyings: UnderlyingSection,
  val generalInfo: GeneralInfo,
  val redemption: Redemption,
  val dates: DatesSection)

class Table

class FoRenderContext

trait TermsheetFoRenderer {
  def render(context: FoRenderContext) = {
    val fo =
      <root xmlns="http://www.w3.org/1999/XSL/Format">
        <layout-master-set>
          <simple-page-master page-height="11in" page-width="8.5in" master-name="only">
            <region-body region-name="xsl-region-body" margin="0.7in"/>
            <region-before region-name="xsl-region-before" extent="0.7in"/>
            <region-after region-name="xsl-region-after" extent="0.7in"/>
          </simple-page-master>
        </layout-master-set>
        <page-sequence master-reference="only" format="A">
          <flow flow-name="xsl-region-body">
            <block>{"Hello " + "world!"}</block>
          </flow>
        </page-sequence>
      </root>
    fo
  }
}