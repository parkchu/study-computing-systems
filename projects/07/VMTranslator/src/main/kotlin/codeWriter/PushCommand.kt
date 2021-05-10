package codeWriter

class PushCommand(index: Int, fileName: String) {
    private val commands: Map<String, List<String>> = mapOf(
        "constant" to listOf("@$index", "D=A"),
        "local" to makeStringsBySegmentAndIndexWhenPush("@LCL", index),
        "argument" to makeStringsBySegmentAndIndexWhenPush("@ARG", index),
        "this" to makeStringsBySegmentAndIndexWhenPush("@THIS", index),
        "that" to makeStringsBySegmentAndIndexWhenPush("@THAT", index),
        "pointer" to makeStringsBySegmentAndIndexWhenPush("@3", index, "A"),
        "temp" to makeStringsBySegmentAndIndexWhenPush("@5", index, "A"),
        "static" to listOf("@$fileName.$index", "D=M")
    )

    private fun makeStringsBySegmentAndIndexWhenPush(segment: String, index: Int, register: String = "M"): List<String> {
        return listOf(segment, "D=$register", "@$index", "A=D+A", "D=M")
    }

    fun makePushCommand(segment: String): List<String> {
        return commands[segment] ?: throw RuntimeException("지원하지 않는 명령어 입니다.")
    }
}
