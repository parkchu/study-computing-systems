package codeWriter

import commands.CallCommand
import java.io.File

class CodeWriter(filePath: String) {
    private val strings: MutableList<String> = mutableListOf()
    private val outputFile = File(filePath)
    private var labels: Int = 0
    var currentFileName: String? = null
        private set
    var functionName: String = ""
        private set
    var callTime: Int = 0
        private set

    fun setFileName(fileName: String) {
        currentFileName = fileName
    }

    fun write(command: String) {
        strings.add(command)
    }

    fun write(commands: List<String>) {
        strings.addAll(commands)
    }

    fun plusLabels() {
        labels += 1
    }

    fun plusCallTime() {
        callTime += 1
    }

    fun updateFunctionName(functionName: String) {
        this.functionName = functionName
    }

    fun getSize(): Int = strings.size - labels

    fun writeInit() {
        val commands = listOf("@256", "D=A", "@SP", "M=D")
        strings.addAll(commands)
        CallCommand().write(this, listOf("Sys.init", "0"))
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
