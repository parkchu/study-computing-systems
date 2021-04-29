package codeWriter

import parser.CommandType
import java.io.File

class CodeWriter(file: File) {
    private val parentDirectoryPath: String = file.parent
    private val strings: MutableList<String> = mutableListOf()
    var outputFile: File? = File("$parentDirectoryPath/${file.nameWithoutExtension}.asm")
        private set

    fun setFileName(fileName: String) {
        val path = "$parentDirectoryPath/$fileName.asm"
        outputFile = File(path)
    }

    fun writeArithmetic(command: String) {
        when (command) {
            "add" -> writeAddSubAndOr("M=M+D")

            "sub" -> writeAddSubAndOr("M=M-D")

            "and" -> writeAddSubAndOr("M=M&D")

            "or" -> writeAddSubAndOr("M=M|D")

            "neg" -> writeNegNot("M=-M")

            "not" -> writeNegNot("M=!M")

            "eq" -> writeEqGtLt("D;JEQ", "D;JNE")

            "gt" -> writeEqGtLt("D;JGT", "D;JLE")

            "lt" -> writeEqGtLt("D;JLT", "D;JGE")
        }
    }

    private fun writeAddSubAndOr(command: String) {
        strings.addAll(listOf("@SP", "AM=M-1", "D=M", "M=0", "A=A-1"))
        strings.add(command)
    }

    private fun writeNegNot(command: String) {
        strings.addAll(listOf("@SP", "A=M-1"))
        strings.add(command)
    }

    private fun writeEqGtLt(jump1: String, jump2: String) {
        strings.addAll(listOf("@${strings.size + 14}", "D=A", "@address", "M=D"))
        writeAddSubAndOr("D=M-D")
        strings.addAll(listOf("@TRUE", jump1, "@FALSE", jump2))
    }

    fun writePushPop(commandType: CommandType, segment: String, index: Int) {
        if (commandType == CommandType.C_PUSH) {
            val strings1 = makeStringsBySegmentWhenPush(segment, index)
            val strings2 = listOf("@SP", "A=M", "M=D", "@SP", "M=M+1")
            strings.addAll(strings1 + strings2)
        }
        if (commandType == CommandType.C_POP) {
            val strings2 = listOf("@SP", "AM=M-1", "D=M", "M=0")
            val strings1 = makeStringsBySegmentWhenPop(segment, index)
            strings.addAll(strings2 + strings1)
        }
    }

    private fun makeStringsBySegmentWhenPush(segment: String, index: Int): List<String> {
        if (segment == "constant") {
            return listOf("@$index", "D=A")
        }
        return listOf()
    }

    private fun makeStringsBySegmentWhenPop(segment: String, index: Int): List<String> {
        return listOf()
    }

    fun close() {
        write()
        outputFile = null
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
