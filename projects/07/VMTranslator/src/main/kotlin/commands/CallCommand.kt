package commands

import codeWriter.CodeWriter
import parser.CommandType

class CallCommand : Command {
    override fun isSupports(command: CommandType): Boolean = command == CommandType.C_CALL

    override fun write(codeWriter: CodeWriter, args: List<String>) {
        val callTime = codeWriter.callTime
        val segments = listOf("LCL", "ARG", "THIS", "THAT")
        val commands1 = listOf(
            "@SP",
            "D=M",
            "@${args[1]}",
            "D=D-A",
            "@5",
            "D=D-A",
            "@ARG",
            "M=D",
            "@SP",
            "D=M",
            "@LCL",
            "M=D"
        )
        val commands2 = listOf("@${args[0]}", "0;JMP")
        val command = "(return-address$callTime)"
        codeWriter.write(listOf("@return-address$callTime", "D=A", "@SP", "A=M", "M=D", "@SP", "M=M+1"))
        segments.forEach { writeSimplePush(it, codeWriter) }
        codeWriter.write(commands1 + commands2)
        codeWriter.write(command)
        codeWriter.plusLabels()
        codeWriter.plusCallTime()
    }

    private fun writeSimplePush(segment: String, codeWriter: CodeWriter) {
        val commands = listOf("@$segment", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1")
        codeWriter.write(commands)
    }
}
