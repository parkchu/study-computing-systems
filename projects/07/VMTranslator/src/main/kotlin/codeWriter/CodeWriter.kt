package codeWriter

import parser.CommandType
import java.io.File

class CodeWriter(file: File) {
    private val strings: MutableList<String> = mutableListOf()
    var outputFile: File? = File("${file.parent}/${file.nameWithoutExtension}.asm")
        private set

    fun setFileName(path: String, fileName: String) {
        outputFile = File("$path/$fileName.asm")
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
            val strings1 = makeStringsBySegmentWhenPop(segment, index)
            val strings2 = listOf("@SP", "AM=M-1", "D=M", "M=0", "@13", "A=M", "M=D")
            strings.addAll(strings1 + strings2)
        }
    }

    private fun makeStringsBySegmentWhenPush(segment: String, index: Int): List<String> {
        return when (segment) {
            "constant" -> {
                listOf("@$index", "D=A")
            }
            "local" -> {
                makeStringsBySegmentAndIndexWhenPush("@LCL", index)
            }
            "argument" -> {
                makeStringsBySegmentAndIndexWhenPush("@ARG", index)
            }
            "this" -> {
                makeStringsBySegmentAndIndexWhenPush("@THIS", index)
            }
            "that" -> {
                makeStringsBySegmentAndIndexWhenPush("@THAT", index)
            }
            "pointer" -> {
                makeStringsBySegmentAndIndexWhenPush("@3", index, "A")
            }
            "temp" -> {
                makeStringsBySegmentAndIndexWhenPush("@5", index, "A")
            }
            "static" -> {
                listOf("@${outputFile?.nameWithoutExtension}.$index", "D=M")
            }
            else -> {
                throw RuntimeException("지원하지 않는 세그먼트 입니다.")
            }
        }
    }

    private fun makeStringsBySegmentAndIndexWhenPush(segment: String, index: Int, register: String = "M"): List<String> {
        return listOf(segment, "D=$register", "@$index", "A=D+A", "D=M")
    }

    private fun makeStringsBySegmentWhenPop(segment: String, index: Int): List<String> {
        return when (segment) {
            "constant" -> {
                listOf("@$index", "A=D")
            }
            "local" -> {
                makeStringsBySegmentAndIndexWhenPop("@LCL", index)
            }
            "argument" -> {
                makeStringsBySegmentAndIndexWhenPop("@ARG", index)
            }
            "this" -> {
                makeStringsBySegmentAndIndexWhenPop("@THIS", index)
            }
            "that" -> {
                makeStringsBySegmentAndIndexWhenPop("@THAT", index)
            }
            "pointer" -> {
                makeStringsBySegmentAndIndexWhenPop("@3", index, "A")
            }
            "temp" -> {
                makeStringsBySegmentAndIndexWhenPop("@5", index, "A")
            }
            "static" -> {
                listOf("@${outputFile?.nameWithoutExtension}.$index", "D=A", "@13", "M=D")
            }
            else -> {
                throw RuntimeException("지원하지 않는 세그먼트 입니다.")
            }
        }
    }

    private fun makeStringsBySegmentAndIndexWhenPop(segment: String, index: Int, register: String = "M"): List<String> {
        return listOf(segment, "D=$register", "@$index", "D=D+A", "@13", "M=D")
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
