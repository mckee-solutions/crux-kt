package solutions.mckee.crux.commands

class DeleteCommand(lineRemainder: String) : XPathOnly(lineRemainder) {
  override fun xslt() = """<xsl:template match="${this.xpath}"></xsl:template>"""
}
