package commands

import codeWriter.CodeWriter
import parser.CommandType

interface Command {
    fun isSupports(command: CommandType): Boolean

    fun write(codeWriter: CodeWriter, args: List<String>)
}
