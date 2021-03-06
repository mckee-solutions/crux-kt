package solutions.mckee.crux.parser

import solutions.mckee.crux.commands.*
import solutions.mckee.crux.exceptions.ParseException
import java.io.InputStream

private var currentLine: Int = 0

data class ScriptInfo(
  val commands: MutableList<CruxCommand> = mutableListOf(),
  val errors: MutableList<String> = mutableListOf()
)

fun parseScript(inputStream: InputStream): ScriptInfo {
  val parsingInfo = ScriptInfo()
  inputStream.buffered().reader().use { reader ->
    reader.forEachLine { line ->
      currentLine++
      if (line.isBlank() || line[0] == '#') {
        // line is a comment or blank, loop
        return@forEachLine
      }
      try {
        val (commandType, ixAfterCommand) = parseCommandType(line)
        val lineRemainder = line.substring(ixAfterCommand).trim()
        parsingInfo.commands.add(
          when (commandType) {
            "append" -> AppendCommand(lineRemainder)
            "delete" -> DeleteCommand(lineRemainder)
            "prepend" -> PrependCommand(lineRemainder)
            "replace" -> ReplaceCommand(lineRemainder)
            else -> throw ParseException("Invalid command: \"$commandType\"")
          }
        )
      }
      catch(e: ParseException) {
        parsingInfo.errors.add("Line $currentLine: ${e.message}")
      }
    }
  }
  return parsingInfo
}

private data class CommandTypeParseResult(val commandType: String, val stoppedAtIndex: Int)
private fun parseCommandType(line: String): CommandTypeParseResult {
  val ixFirstSpace = line.indexOfFirst { c -> c.isWhitespace() }
  return CommandTypeParseResult(line.substring(0,ixFirstSpace), ixFirstSpace)
}
