package parser

enum class CommandType(val command: String) {
    C_ARITHMETIC("add sub neg eq gt lt and or not"),
    C_PUSH("push"),
    C_POP("pop"),
    C_LABEL("label"),
    C_GOTO("goto"),
    C_IF("if-goto"),
    C_FUNCTION("function"),
    C_RETURN("return"),
    C_CALL("call");

    companion object {
        fun findByCommand(command: String): CommandType {
            return values().find { it.command == command } ?: throw RuntimeException("해당 명령어는 지원하지 않습니다.")
        }
    }
}
