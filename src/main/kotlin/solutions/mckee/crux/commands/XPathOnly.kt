package solutions.mckee.crux.commands

import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory

interface XPathOnly : CruxCommand {
  val xpath: String
  fun parseLineParams(lineRemainder: String): String {
    val xpathFactory = XPathFactory.newInstance()
    // this will throw an exception if an invalid xpath is found
    xpathFactory.newXPath().compile(lineRemainder)
    return lineRemainder
  }
}
