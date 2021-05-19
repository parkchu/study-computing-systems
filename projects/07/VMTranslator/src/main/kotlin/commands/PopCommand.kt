package commands

import codeWriter.CodeWriter
import parser.CommandType

class PopCommand : Command {
    override fun isSupports(command: CommandType): Boolean = command == CommandType.C_POP

    override fun write(codeWriter: CodeWriter, args: List<String>) {
        val fileName = codeWriter.currentFileName ?: throw RuntimeException("파일이 설정되어 있지 않습니다.")
        val popCommands = makeCommands(args[1].toInt(), fileName)
        val strings1 = popCommands[args[0]] ?: throw RuntimeException("지원하지 않는 명령어 입니다.")
        val strings2 = listOf("@SP", "AM=M-1", "D=M", "M=0", "@13", "A=M", "M=D")
        codeWriter.write(strings1 + strings2)
    }

    private fun makeCommands(index: Int, fileName: String): Map<String, List<String>> {
        return mapOf(
            "constant" to listOf("@$index", "A=D"),
            "local" to makePop("@LCL", index),
            "argument" to makePop("@ARG", index),
            "this" to makePop("@THIS", index),
            "that" to makePop("@THAT", index),
            "pointer" to makePop("@3", index, "A"),
            "temp" to makePop("@5", index, "A"),
            "static" to listOf("@$fileName.$index", "D=A", "@13", "M=D")
        )
    }

    private fun makePop(segment: String, index: Int, register: String = "M"): List<String> {
        return listOf(segment, "D=$register", "@$index", "D=D+A", "@13", "M=D")
    }
}
