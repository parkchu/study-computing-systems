package codeWriter

class ArithmeticCommand(strings: List<String>) {
    private val commands: Map<String, List<String>> = mapOf(
        "add" to writeAddSubAndOr("M=M+D"),
        "sub" to writeAddSubAndOr("M=M-D"),
        "and" to writeAddSubAndOr("M=M&D"),
        "or" to writeAddSubAndOr("M=M|D"),
        "neg" to writeNegNot("M=-M"),
        "not" to writeNegNot("M=!M"),
        "eq" to writeEqGtLt(strings.size, "D;JEQ", "D;JNE"),
        "gt" to writeEqGtLt(strings.size, "D;JGT", "D;JLE"),
        "lt" to writeEqGtLt(strings.size,"D;JLT", "D;JGE")
    )

    private fun writeAddSubAndOr(command: String) = listOf("@SP", "AM=M-1", "D=M", "M=0", "A=A-1", command)

    private fun writeNegNot(command: String) = listOf("@SP", "A=M-1", command)

    private fun writeEqGtLt(size: Int, jump1: String, jump2: String): List<String> {
        val commands = mutableListOf<String>()
        commands.addAll(listOf("@${size + 14}", "D=A", "@address", "M=D"))
        commands.addAll(writeAddSubAndOr("D=M-D"))
        commands.addAll(listOf("@TRUE", jump1, "@FALSE", jump2))
        return commands
    }

    fun makeArithmeticCommand(command: String): List<String> {
        return commands[command] ?: throw RuntimeException("지원하지 않는 명령어 입니다.")
    }
}
