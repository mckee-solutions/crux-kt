package solutions.mckee.crux

import picocli.CommandLine
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import solutions.mckee.crux.parser.parseScript
import solutions.mckee.crux.xslt.createXsltScript
import solutions.mckee.crux.xslt.executeScript
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

fun main(args: Array<String>) {
  val app = CommandLine.populateCommand(CruxApplication(), *args)
  handleHelpRequested(app)
  assertScriptProvided(app)
  val scriptInfo = parseScript(app.script)
  if (scriptInfo.errors.isNotEmpty()) {
    scriptInfo.errors.forEach { System.err.println(it) }
    System.exit(1)
  }
  val xsltScript = createXsltScript(scriptInfo)
  val xmlInput = getInputStream(app)
  executeScript(xmlInput, xsltScript)
}

class CruxApplication {

  @Option(names = ["-f", "--file"], paramLabel = "SCRIPT_FILE", description = ["CRUX script file"])
  var scriptFile: File? = null

  @Option(names = ["-s", "--script"], paramLabel = "SCRIPT", description = ["CRUX script as a string"])
  var scriptString: String? = null

  @Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message and exit"])
  var usageHelpRequested: Boolean = false

  @Parameters(
    paramLabel = "FILE",
    arity = "0..1",
    description = ["File to process. If a file is provided, standard input will be ignored."]
  )
  var inputFile: File? = null

  val script: InputStream
  get() = when {
      scriptFile != null -> scriptFile!!.inputStream()
      scriptString != null -> scriptString!!.byteInputStream()
      else -> InputStream.nullInputStream()
  }

}

fun assertScriptProvided(app: CruxApplication) {
  if (app.scriptFile == null && app.scriptString == null) {
    System.err.println("Either one of -f or -s must be specified.")
    CommandLine.usage(app, System.err)
    System.exit(1)
  }
  if (app.scriptFile?.length() == 0L) {
    System.err.println("The script file specified could not be found or it is empty.")
    System.exit(1)
  }
}

fun handleHelpRequested(app: CruxApplication) {
  if (app.usageHelpRequested) {
    CommandLine.usage(app, System.err)
    System.exit(0)
  }
}

fun getInputStream(app: CruxApplication): InputStream = when {
  app.inputFile != null -> FileInputStream(app.inputFile)
  else -> System.`in`
}
