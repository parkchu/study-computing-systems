package jackTokenizer

class Token(val type: TokenType, val value: String)

enum class TokenType(val value: String) {
    KEYWORD("keyword"),
    SYMBOL("symbol"),
    IDENTIFIER("identifier"),
    INT_CONST("integerConstant"),
    STRING_CONST("stringConstant")
}

enum class Keyword(val value: String) {
    CLASS("class"),
    CONSTRUCTOR("constructor"),
    FUNCTION("function"),
    METHOD("method"),
    FIELD("field"),
    STATIC("static"),
    VAR("var"),
    INT("int"),
    CHAR("char"),
    BOOLEAN("boolean"),
    VOID("void"),
    TRUE("true"),
    FALSE("false"),
    NULL("null"),
    THIS("this"),
    LET("let"),
    DO("do"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    RETURN("return");

    companion object {
        fun containsIt(value: String): Boolean = values().any { it.value == value }

        fun find(value: String): Keyword = values().find { it.value == value } ?: throw RuntimeException("")
    }
}