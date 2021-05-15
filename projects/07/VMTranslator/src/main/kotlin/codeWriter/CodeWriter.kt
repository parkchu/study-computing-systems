package codeWriter

import java.io.File

class CodeWriter(filePath: String) {
    private val strings: MutableList<String> = mutableListOf()
    private val outputFile = File(filePath)
    private var currentFileName: String? = null
    private var functionName: String = ""
    private var callTime: Int = 0
    private var labels: Int = 0

    fun setFileName(fileName: String) {
        currentFileName = fileName
    }

    fun writeArithmetic(command: String) {
        val arithmeticCommand = ArithmeticCommand(strings.size - labels).makeArithmeticCommand(command)
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

    fun writeInit() {
        val commands = listOf("@256", "D=A", "@SP", "M=D")
        strings.addAll(commands)
        writeCall("Sys.init", 0)
    }


    fun writeLabel(label: String) {
        val command = "($functionName$$label)"
        strings.add(command)
        labels += 1
    }

    fun writeGoto(label: String) {
        val commands = listOf("@$functionName$$label", "0;JMP")
        strings.addAll(commands)
    }

    fun writeIf(label: String) {
        val commands = listOf("@SP", "AM=M-1", "D=M", "M=0", "@$functionName$$label", "D;JNE")
        strings.addAll(commands)
    }

    fun writeCall(functionName: String, numArgs: Int) {
        val segments = listOf("LCL", "ARG", "THIS", "THAT")
        val commands1 = listOf("@SP", "D=M", "@$numArgs", "D=D-A", "@5", "D=D-A", "@ARG", "M=D", "@SP", "D=M", "@LCL", "M=D")
        val commands2 = listOf("@$functionName", "0;JMP")
        val command = "(return-address$callTime)"
        strings.addAll(listOf("@return-address$callTime", "D=A", "@SP", "A=M", "M=D", "@SP", "M=M+1"))
        segments.forEach { writeSimplePush(it) }
        strings.addAll(commands1 + commands2)
        strings.add(command)
        callTime += 1
        labels += 1
    }

    private fun writeSimplePush(segment: String) {
        val commands = listOf("@$segment", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1")
        strings.addAll(commands)
    }

    fun writeFunction(functionName: String, numLocals: Int) {
        this.functionName = functionName
        val command = "($functionName)"
        val commands = listOf("@SP", "D=M", "M=M+1", "A=D", "M=0")
        strings.add(command)
        repeat(numLocals) { strings.addAll(commands) }
        labels += 1
    }

    fun writeReturn() {
        val segments = listOf("THAT", "THIS", "ARG", "LCL")
        val commands1 = listOf("@LCL", "D=M", "@14", "M=D", "@5", "A=D-A", "D=M", "@15", "M=D")
        val commands2 = listOf("@SP", "AM=M-1", "D=M", "M=0", "@ARG", "A=M", "M=D", "D=A", "@SP", "M=D+1")
        val commands3 = listOf("@15", "A=M", "0;JMP")
        strings.addAll(commands1 + commands2)
        segments.forEach { setValue(it, segments.indexOf(it)) }
        strings.addAll(commands3)
    }

    private fun setValue(segment: String, index: Int) {
        val commands = listOf("@14", "D=M", "@${index+1}", "A=D-A", "D=M", "@$segment", "M=D")
        strings.addAll(commands)
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
