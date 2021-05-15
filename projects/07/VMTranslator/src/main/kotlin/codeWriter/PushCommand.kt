package codeWriter

class PushCommand(index: Int, fileName: String) {
    private val commands: Map<String, List<String>> = mapOf(
        "constant" to listOf("@$index", "D=A"),
        "local" to makePush("@LCL", index),
        "argument" to makePush("@ARG", index),
        "this" to makePush("@THIS", index),
        "that" to makePush("@THAT", index),
        "pointer" to makePush("@3", index, "A"),
        "temp" to makePush("@5", index, "A"),
        "static" to listOf("@$fileName.$index", "D=M")
    )

    private fun makePush(segment: String, index: Int, register: String = "M"): List<String> {
        return listOf(segment, "D=$register", "@$index", "A=D+A", "D=M")
    }

    fun makePushCommand(segment: String): List<String> {
        return commands[segment] ?: throw RuntimeException("지원하지 않는 명령어 입니다.")
    }
}
