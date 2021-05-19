package commands

import codeWriter.CodeWriter
import parser.CommandType

class LabelCommand : Command {
    override fun isSupports(command: CommandType): Boolean = command == CommandType.C_LABEL

    override fun write(codeWriter: CodeWriter, args: List<String>) {
        val functionName = codeWriter.functionName
        val command = "($functionName$${args[0]})"
        codeWriter.write(command)
        codeWriter.plusLabels()
    }
}
