package codeWriter

import java.io.File

class CodeWriter(filePath: String) {
    private val strings: MutableList<String> = mutableListOf()
    private val outputFile = File(filePath)
    private var currentFileName: String? = null

    fun setFileName(fileName: String) {
        currentFileName = fileName
    }

    fun writeArithmetic(command: String) {
        val arithmeticCommand = ArithmeticCommand(strings).makeArithmeticCommand(command)
        strings.addAll(arithmeticCommand)
    }

    fun writePush(segment: String, index: Int) {
        val fileName = currentFileName ?: throw RuntimeException("파일이 설정되어 있지 않습니다.")
        val pushCommand = PushCommand(index, fileName)
        val strings1 = pushCommand.makePushCommand(segment)
        val strings2 = listOf("@SP", "A=M", "M=D", "@SP", "M=M+1")
        strings.addAll(strings1 + strings2)
    }

    fun writePop(segment: String, index: Int) {
        val fileName = currentFileName ?: throw RuntimeException("파일이 설정되어 있지 않습니다.")
        val popCommand = PopCommand(index, fileName)
        val strings1 = popCommand.makePopCommand(segment)
        val strings2 = listOf("@SP", "AM=M-1", "D=M", "M=0", "@13", "A=M", "M=D")
        strings.addAll(strings1 + strings2)
    }

    fun close() {
        write()
        currentFileName = null
        strings.clear()
    }

    private fun write() {
        writeBasicCommand()
        outputFile.bufferedWriter().use { writer ->
            strings.forEach {
                writer.write(it)
                writer.newLine()
            }
        }
    }

    private fun writeBasicCommand() {
        strings.addAll(
            listOf(
                "@END",
                "0;JMP",
                "(TRUE)",
                "@SP",
                "A=M-1",
                "M=-1",
                "@address",
                "A=M",
                "0;JMP",
                "(FALSE)",
                "@SP",
                "A=M-1",
                "M=0",
                "@address",
                "A=M",
                "0;JMP",
                "(END)",
                "@END",
                "0;JMP"
            )
        )
    }
}
