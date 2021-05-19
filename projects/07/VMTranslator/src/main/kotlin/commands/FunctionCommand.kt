package commands

import codeWriter.CodeWriter
import parser.CommandType

class FunctionCommand : Command {
    override fun isSupports(command: CommandType): Boolean = command == CommandType.C_FUNCTION

    override fun write(codeWriter: CodeWriter, args: List<String>) {
        codeWriter.updateFunctionName(args[0])
        val command = "(${codeWriter.functionName})"
        val commands = listOf("@SP", "D=M", "M=M+1", "A=D", "M=0")
        codeWriter.write(command)
        repeat(args[1].toInt()) { codeWriter.write(commands) }
        codeWriter.plusLabels()
    }
}
