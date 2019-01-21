package solutions.mckee.crux.commands

import net.sf.saxon.type.UType
import solutions.mckee.crux.exceptions.ParseException
import solutions.mckee.crux.parser.getXPathInfo

class ReplaceCommand(lineRemainder: String) : XPathAndValue(lineRemainder) {
  override fun xslt() = when (getXPathInfo(this.xpath).resultType) {
    UType.ATTRIBUTE -> """
          <xsl:template match="${this.xpath}">
            <xsl:attribute name="{name()}">
              <xsl:text>${this.value}</xsl:text>
            </xsl:attribute>
          </xsl:template>
      """.trimIndent()
    UType.ELEMENT -> """<xsl:template match="${this.xpath}">${this.value}</xsl:template>"""
    else -> throw ParseException("Unsupported location XPath for replace command: ${this.xpath}")
  }
}
