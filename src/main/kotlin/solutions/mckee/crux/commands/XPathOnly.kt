package solutions.mckee.crux.commands

import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory

abstract class XPathOnly(lineRemainder: String) : CruxCommand {
  val xpath: String = this.parseLineParams(lineRemainder)
  private fun parseLineParams(lineRemainder: String): String {
    val xpathFactory = XPathFactory.newInstance()
    // this will throw an exception if an invalid xpath is found
    xpathFactory.newXPath().compile(lineRemainder)
    return lineRemainder
  }
}
