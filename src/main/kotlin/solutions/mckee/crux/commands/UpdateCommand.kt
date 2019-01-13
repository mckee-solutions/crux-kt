package solutions.mckee.crux.commands

import net.sf.saxon.type.UType
import solutions.mckee.crux.parser.getXPathInfo

class UpdateCommand(lineRemainder: String) : XPathAndValue(lineRemainder) {
  override fun xslt() = when (getXPathInfo(this.xpath).resultType) {
    UType.ATTRIBUTE -> """
          <xsl:template match="${this.xpath}">
            <xsl:attribute name="{name()}">
              <xsl:text>${this.value}</xsl:text>
            </xsl:attribute>
          </xsl:template>
      """.trimIndent()
    else -> """<xsl:template match="${this.xpath}/text()">${this.value}</xsl:template>"""
  }
}
