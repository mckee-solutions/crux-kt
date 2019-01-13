package solutions.mckee.crux.parser

import net.sf.saxon.s9api.ItemType
import net.sf.saxon.s9api.Processor
import net.sf.saxon.type.UType

private val processor = Processor(false)
private val xPathCompiler = processor.newXPathCompiler()

data class XPathInfo(val resultType: UType, val text: String)
fun getXPathInfo(text: String) = XPathInfo(xPathCompiler.compile(text).resultItemType.underlyingItemType.uType, text)
