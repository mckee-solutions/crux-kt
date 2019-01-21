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

/**
 * Main method for the CRUX application.
 *
 * CRUX is a command-line application that takes an XML document as a stream
 * and outputs a new XML document with changes specified in a crux script.
 *
 */
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

/**
 * This class defines the command-line parameters and options that can be
 * used to run the application.
 */
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

/**
 * Check to see if the user requested help instead of trying to run the application/scripts
 *
 * @param app The application instance to check
 */
fun handleHelpRequested(app: CruxApplication) {
  if (app.usageHelpRequested) {
    CommandLine.usage(app, System.err)
    System.exit(0)
  }
}

/**
 * Ensure a crux script was provided to the program
 *
 * @param app The application instance to check
 */
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

/**
 * Retrieve the input XML to the application. This can come as either
 * standard input to the application or passed as a file parameter.
 *
 * @param app The application instance to read from
 * @return the input stream of the file if one was provided, or the input
 * stream as read from standard input.
 */
fun getInputStream(app: CruxApplication): InputStream = when {
  app.inputFile != null -> FileInputStream(app.inputFile)
  else -> System.`in`
}
