package codeWriter

import java.io.File

class CodeWriter(file: File) {
    private val strings: MutableList<String> = mutableListOf()
    var outputFile: File? = File("${file.parent}/${file.nameWithoutExtension}.asm")
        private set

    fun setFileName(path: String, fileName: String) {
        outputFile = File("$path/$fileName.asm")
    }

    fun writeArithmetic(command: String) {
        val arithmeticCommand = ArithmeticCommand(strings).makeArithmeticCommand(command)
        strings.addAll(arithmeticCommand)
    }

    fun writePush(segment: String, index: Int) {
        val fileName = outputFile?.nameWithoutExtension ?: throw RuntimeException("파일이 설정되어 있지 않습니다.")
        val pushCommand = PushCommand(index, fileName)
        val strings1 = pushCommand.makePushCommand(segment)
        val strings2 = listOf("@SP", "A=M", "M=D", "@SP", "M=M+1")
        strings.addAll(strings1 + strings2)
    }

    fun writePop(segment: String, index: Int) {
        val fileName = outputFile?.nameWithoutExtension ?: throw RuntimeException("파일이 설정되어 있지 않습니다.")
        val popCommand = PopCommand(index, fileName)
        val strings1 = popCommand.makePopCommand(segment)
        val strings2 = listOf("@SP", "AM=M-1", "D=M", "M=0", "@13", "A=M", "M=D")
        strings.addAll(strings1 + strings2)
    }

    fun close() {
        write()
        outputFile = null
        strings.clear()
    }

    private fun write() {
        val file = outputFile ?: throw RuntimeException("입력할 파일이 없습니다.")
        writeBasicCommand()
        file.bufferedWriter().use { writer ->
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
