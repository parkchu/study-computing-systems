package commands

import codeWriter.CodeWriter
import parser.CommandType

class GotoCommand : Command {
    override fun isSupports(command: CommandType): Boolean = command == CommandType.C_GOTO

    override fun write(codeWriter: CodeWriter, args: List<String>) {
        val functionName = codeWriter.functionName
        val commands = listOf("@$functionName$${args[0]}", "0;JMP")
        codeWriter.write(commands)
    }
}
