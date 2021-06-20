package symbolTable

enum class Kind(val value: String) {
    STATIC("static"),
    FIELD("field"),
    ARG("argument"),
    VAR("var"),
    NONE("");

    companion object {
        fun findIt(kind: String): Kind = values().find { it.value == kind } ?: NONE
    }
}
