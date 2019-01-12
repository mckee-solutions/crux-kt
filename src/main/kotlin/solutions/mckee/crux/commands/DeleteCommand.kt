package solutions.mckee.crux.commands

class DeleteCommand(lineRemainder: String) : XPathOnly {
  override val xpath: String = this.parseLineParams(lineRemainder)
  override fun xslt() = """<xsl:template match="${this.xpath}"></xsl:template>"""
}
