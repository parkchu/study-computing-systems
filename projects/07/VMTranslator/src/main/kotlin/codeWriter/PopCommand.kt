package codeWriter

class PopCommand(index: Int, fileName: String) {
    private val commands: Map<String, List<String>> = mapOf(
        "constant" to listOf("@$index", "A=D"),
        "local" to makePop("@LCL", index),
        "argument" to makePop("@ARG", index),
        "this" to makePop("@THIS", index),
        "that" to makePop("@THAT", index),
        "pointer" to makePop("@3", index, "A"),
        "temp" to makePop("@5", index, "A"),
        "static" to listOf("@$fileName.$index", "D=A", "@13", "M=D")
    )

    private fun makePop(segment: String, index: Int, register: String = "M"): List<String> {
        return listOf(segment, "D=$register", "@$index", "D=D+A", "@13", "M=D")
    }

    fun makePopCommand(segment: String): List<String> {
        return commands[segment] ?: throw RuntimeException("지원하지 않는 명령어 입니다.")
    }
}
