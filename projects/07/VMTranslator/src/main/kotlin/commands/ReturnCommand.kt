package commands

import codeWriter.CodeWriter
import parser.CommandType

class ReturnCommand : Command {
    override fun isSupports(command: CommandType): Boolean = command == CommandType.C_RETURN

    override fun write(codeWriter: CodeWriter, args: List<String>) {
        val segments = listOf("THAT", "THIS", "ARG", "LCL")
        val commands1 = listOf("@LCL", "D=M", "@14", "M=D", "@5", "A=D-A", "D=M", "@15", "M=D")
        val commands2 = listOf("@SP", "AM=M-1", "D=M", "M=0", "@ARG", "A=M", "M=D", "D=A", "@SP", "M=D+1")
        val commands3 = listOf("@15", "A=M", "0;JMP")
        codeWriter.write(commands1 + commands2)
        segments.forEach { setValue(it, segments.indexOf(it), codeWriter) }
        codeWriter.write(commands3)
    }

    private fun setValue(segment: String, index: Int, codeWriter: CodeWriter) {
        val commands = listOf("@14", "D=M", "@${index+1}", "A=D-A", "D=M", "@$segment", "M=D")
        codeWriter.write(commands)
    }
}
