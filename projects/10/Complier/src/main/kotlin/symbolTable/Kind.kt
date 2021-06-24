package symbolTable

enum class Kind(val value: String) {
    STATIC("static"),
    FIELD("this"),
    ARG("argument"),
    VAR("local"),
    NONE("");

    companion object {
        fun findIt(kind: String): Kind {
            if (kind == "field") {
                return FIELD
            }
            return values().find { it.value == kind } ?: NONE
        }
    }
}
