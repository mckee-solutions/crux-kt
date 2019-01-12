package solutions.mckee.crux.commands

import javax.xml.xpath.XPathExpression

interface CruxCommand {

  /**
   * Create an XSLT node for this command
   *
   * @return The XSLT to add as a String
   */
  fun xslt(): String
}
