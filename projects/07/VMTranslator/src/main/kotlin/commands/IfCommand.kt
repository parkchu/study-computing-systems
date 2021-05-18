package commands

import codeWriter.CodeWriter
import parser.CommandType

class IfCommand : Command {
    override fun isSupports(command: CommandType): Boolean = command == CommandType.C_IF

    override fun write(codeWriter: CodeWriter, args: List<String>) {
        val functionName = codeWriter.functionName
        val commands = listOf("@SP", "AM=M-1", "D=M", "M=0", "@$functionName$${args[0]}", "D;JNE")
        codeWriter.write(commands)
    }
}
