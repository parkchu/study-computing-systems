package commands

import codeWriter.CodeWriter
import parser.CommandType

class PushCommand : Command {
    override fun isSupports(command: CommandType): Boolean = command == CommandType.C_PUSH

    override fun write(codeWriter: CodeWriter, args: List<String>) {
        val fileName = codeWriter.currentFileName ?: throw RuntimeException("파일이 설정되어 있지 않습니다.")
        val pushCommands = makeCommands(args[1].toInt(), fileName)
        val strings1 = pushCommands[args[0]] ?: throw RuntimeException("지원하지 않는 명령어 입니다.")
        val strings2 = listOf("@SP", "A=M", "M=D", "@SP", "M=M+1")
        codeWriter.write(strings1 + strings2)
    }

    private fun makeCommands(index: Int, fileName: String): Map<String, List<String>> {
        return mapOf(
            "constant" to listOf("@$index", "D=A"),
            "local" to makePush("@LCL", index),
            "argument" to makePush("@ARG", index),
            "this" to makePush("@THIS", index),
            "that" to makePush("@THAT", index),
            "pointer" to makePush("@3", index, "A"),
            "temp" to makePush("@5", index, "A"),
            "static" to listOf("@$fileName.$index", "D=M")
        )
    }

    private fun makePush(segment: String, index: Int, register: String = "M"): List<String> {
        return listOf(segment, "D=$register", "@$index", "A=D+A", "D=M")
    }
}
