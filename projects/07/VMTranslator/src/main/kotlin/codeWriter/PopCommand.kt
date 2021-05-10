package codeWriter

class PopCommand(index: Int, fileName: String) {
    private val commands: Map<String, List<String>> = mapOf(
        "constant" to listOf("@$index", "A=D"),
        "local" to makeStringsBySegmentAndIndexWhenPop("@LCL", index),
        "argument" to makeStringsBySegmentAndIndexWhenPop("@ARG", index),
        "this" to makeStringsBySegmentAndIndexWhenPop("@THIS", index),
        "that" to makeStringsBySegmentAndIndexWhenPop("@THAT", index),
        "pointer" to makeStringsBySegmentAndIndexWhenPop("@3", index, "A"),
        "temp" to makeStringsBySegmentAndIndexWhenPop("@5", index, "A"),
        "static" to listOf("@$fileName.$index", "D=A", "@13", "M=D")
    )

    private fun makeStringsBySegmentAndIndexWhenPop(segment: String, index: Int, register: String = "M"): List<String> {
        return listOf(segment, "D=$register", "@$index", "D=D+A", "@13", "M=D")
    }

    fun makePopCommand(segment: String): List<String> {
        return commands[segment] ?: throw RuntimeException("지원하지 않는 명령어 입니다.")
    }
}
