package solutions.mckee.crux.commands

import net.sf.saxon.type.UType
import solutions.mckee.crux.exceptions.ParseException
import solutions.mckee.crux.parser.getXPathInfo
import solutions.mckee.crux.utils.contains

class PrependCommand(lineRemainder: String) : XPathAndValue(lineRemainder) {

  override fun xslt() = when (getXPathInfo(this.xpath).resultType) {
    UType.ELEMENT -> when (this.value) {
      // value is "@attribute=value"
      in Regex("""^@\S+=.*$""", RegexOption.IGNORE_CASE) -> {
        val splitVal = this.value.split("""=""".toRegex(), 1)
        val attrName = splitVal[0].substring(1)
        val attrValue = splitVal[1]
        """
            <xsl:template match="${this.xpath}">
            <xsl:attribute name="$attrName">
              <xsl:text>$attrValue</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates/>
          </xsl:template>
          """.trimIndent()
      }
      else -> """
          <xsl:template match="${this.xpath}">
            ${this.value}
            <xsl:apply-templates/>
          </xsl:template>
          """.trimIndent()
    }
    else -> throw ParseException("Unsupported location XPath for 'append' command: ${this.xpath}")
  }

}
