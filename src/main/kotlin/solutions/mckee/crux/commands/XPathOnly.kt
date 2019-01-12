package solutions.mckee.crux.commands

import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory

interface XPathOnly : CruxCommand {
  val xpath: XPathExpression
  fun parseLineParams(lineRemainder: String) = XPathFactory.newInstance().newXPath().compile(lineRemainder)
}
