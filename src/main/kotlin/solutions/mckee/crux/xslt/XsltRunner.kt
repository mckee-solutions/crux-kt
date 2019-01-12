package solutions.mckee.crux.xslt

import net.sf.saxon.s9api.Processor
import net.sf.saxon.s9api.Serializer
import java.io.InputStream
import java.io.StringReader
import javax.xml.transform.stream.StreamSource

private val processor = Processor(false)
private val xsltCompiler = processor.newXsltCompiler()

fun executeScript(xmlInput: InputStream, xsltScript: String) {
  val source = StreamSource(xmlInput)
  val xsltExecutable = xsltCompiler.compile(StreamSource(StringReader(xsltScript), "http://localhost/string"))
  val transformer = xsltExecutable.load30()
  val outputSerializer = processor.newSerializer(System.out)
  transformer.transform(source, outputSerializer)
}
