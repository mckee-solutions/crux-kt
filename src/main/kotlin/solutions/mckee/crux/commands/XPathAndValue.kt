package solutions.mckee.crux.commands

import solutions.mckee.crux.exceptions.ParseException
import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathExpressionException
import javax.xml.xpath.XPathFactory

abstract class XPathAndValue(lineRemainder: String) : CruxCommand {
  val xpath: String
  val value: String

  init {
    val (xpath, value) = this.parseLineParams(lineRemainder)
    this.xpath = xpath
    this.value = value
  }

  data class XPathAndValueParams(val xpath: String, val value: String)

  private fun parseLineParams(lineRemainder: String): XPathAndValueParams {
    val (xpath, ixAfterXPath) = parseXPath(lineRemainder)
    return XPathAndValueParams(xpath, lineRemainder.substring(ixAfterXPath).trim())
  }

  private data class XPathParseResult(val xpath: String, val stoppedAtIndex: Int)

  private fun parseXPath(remainder: String): XPathParseResult {
    val xpathFactory = XPathFactory.newInstance()
    for (i in 1..remainder.length) {
      if (i == remainder.length || remainder[i].isWhitespace()) {
        try {
          val xpathToTry = remainder.substring(0, i)
          xpathFactory.newXPath().compile(xpathToTry)
          return XPathParseResult(xpathToTry, i)
        } catch (e: XPathExpressionException) {
          // do nothing
        }
      }
    }
    throw ParseException("No valid XPath found")
  }

}
