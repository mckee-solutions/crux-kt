package solutions.mckee.crux.xslt

import solutions.mckee.crux.parser.ScriptInfo

private val xsltDocHead = """
    <?xml version="1.0" encoding="UTF-8"?>
    <xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:mode on-no-match="shallow-copy" />
""".trimIndent()

private val xsltDocTail = "</xsl:stylesheet>"

fun createXsltScript(scriptInfo: ScriptInfo) = listOf(
  xsltDocHead,
  scriptInfo.commands.map {
    it.xslt()
  }.joinToString(""),
  xsltDocTail
).joinToString("")
