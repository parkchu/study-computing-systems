package commands

import codeWriter.CodeWriter
import parser.CommandType

class ArithmeticCommand : Command {
    override fun isSupports(command: CommandType): Boolean = command == CommandType.C_ARITHMETIC

    override fun write(codeWriter: CodeWriter, args: List<String>) {
        val arithmeticCommands = makeCommands(codeWriter.getSize())
        val commands = arithmeticCommands[args[0]] ?: throw RuntimeException("지원하지 않는 명령어 입니다.")
        codeWriter.write(commands)
    }

    private fun makeCommands(size: Int): Map<String, List<String>> {
        return mapOf(
            "add" to writeAddSubAndOr("M=D+M"),
            "sub" to writeAddSubAndOr("M=M-D"),
            "and" to writeAddSubAndOr("M=D&M"),
            "or" to writeAddSubAndOr("M=D|M"),
            "neg" to writeNegNot("M=-M"),
            "not" to writeNegNot("M=!M"),
            "eq" to writeEqGtLt(size, "D;JEQ", "D;JNE"),
            "gt" to writeEqGtLt(size, "D;JGT", "D;JLE"),
            "lt" to writeEqGtLt(size,"D;JLT", "D;JGE")
        )
    }

    private fun writeAddSubAndOr(command: String) = listOf("@SP", "AM=M-1", "D=M", "M=0", "A=A-1", command)

    private fun writeNegNot(command: String) = listOf("@SP", "A=M-1", command)

    private fun writeEqGtLt(size: Int, jump1: String, jump2: String): List<String> {
        val commands = mutableListOf<String>()
        commands.addAll(listOf("@${size + 14}", "D=A", "@address", "M=D"))
        commands.addAll(writeAddSubAndOr("D=M-D"))
        commands.addAll(listOf("@TRUE", jump1, "@FALSE", jump2))
        return commands
    }
}
