package solutions.mckee.crux.parser

import net.sf.saxon.s9api.ItemType
import net.sf.saxon.s9api.Processor

private val processor = Processor(false)
private val xPathCompiler = processor.newXPathCompiler()

data class XPathInfo(val resultType: ItemType, val text: String)
fun getXPathInfo(text: String) = XPathInfo(xPathCompiler.compile(text).resultItemType, text)
