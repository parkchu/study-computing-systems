package jackTokenizer

interface Token {
    val type: TokenType
    var value: String

    fun isIt(string: String): Boolean
}

class KeywordToken : Token {
    override val type: TokenType = TokenType.KEYWORD
    override var value: String = ""

    override fun isIt(string: String): Boolean {
        value = string.dropLast(1)
        return Keyword.containsIt(value)
    }
}

class IdentifierToken : Token {
    override val type: TokenType = TokenType.IDENTIFIER
    override var value: String = ""

    override fun isIt(string: String): Boolean {
        value = string.dropLast(1)
        return !checkNum(value)
    }
}

class IntValToken : Token {
    override val type: TokenType = TokenType.INT_CONST
    override var value: String = ""

    override fun isIt(string: String): Boolean {
        value = string.dropLast(1)
        return checkNum(value)
    }
}

class StringValToken : Token {
    override val type: TokenType = TokenType.STRING_CONST
    override var value: String = ""

    override fun isIt(string: String): Boolean {
        value = string.dropLast(1)
        return string.last() == '"'
    }
}

class SymbolToken : Token {
    override val type: TokenType = TokenType.SYMBOL
    override var value: String = ""

    override fun isIt(string: String): Boolean {
        value = string
        return false
    }
}

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

fun checkNum(string: String) : Boolean {
    var result = true
    string.forEach {
        if (it.toInt() < 48 || it.toInt() > 57) {
            result = false
        }
    }
    return result
}
