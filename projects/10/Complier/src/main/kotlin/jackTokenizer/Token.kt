package jackTokenizer

interface Token {
    val type: TokenType
    var value: String

    fun isIt(string: String): Boolean

    fun isTerm(): Boolean = false

    fun isUnaryOp(): Boolean = false

    fun isOp(): Boolean = false

    fun isType(): Boolean = false

    fun isKeywordIt(keyword: Keyword): Boolean = false

    fun isKeywordIts(keywords: List<Keyword>): Boolean = false

    fun isIdentifier(): Boolean = false

    fun isSymbolIt(symbol: Char): Boolean = false
}

class KeywordToken : Token {
    override val type: TokenType = TokenType.KEYWORD
    override var value: String = ""

    override fun isIt(string: String): Boolean {
        value = string.dropLast(1)
        return Keyword.containsIt(value)
    }

    override fun isKeywordIt(keyword: Keyword): Boolean {
        return Keyword.find(value) == keyword
    }

    override fun isKeywordIts(keywords: List<Keyword>): Boolean {
        return keywords.contains(Keyword.find(value))
    }

    override fun isTerm(): Boolean = isKeywordIts(listOf(Keyword.TRUE, Keyword.FALSE, Keyword.NULL, Keyword.THIS))

    override fun isType(): Boolean = isKeywordIts(listOf(Keyword.INT, Keyword.CHAR, Keyword.BOOLEAN))
}

class IdentifierToken : Token {
    override val type: TokenType = TokenType.IDENTIFIER
    override var value: String = ""

    override fun isIt(string: String): Boolean {
        value = string.dropLast(1)
        return !checkNum(value)
    }

    override fun isIdentifier(): Boolean = true

    override fun isTerm(): Boolean = true

    override fun isType(): Boolean = true
}

class IntValToken : Token {
    override val type: TokenType = TokenType.INT_CONST
    override var value: String = ""

    override fun isIt(string: String): Boolean {
        value = string.dropLast(1)
        return checkNum(value)
    }

    override fun isTerm(): Boolean = true
}

class StringValToken : Token {
    override val type: TokenType = TokenType.STRING_CONST
    override var value: String = ""

    override fun isIt(string: String): Boolean {
        value = string.dropLast(1)
        return string.last() == '"'
    }

    override fun isTerm(): Boolean = true
}

class SymbolToken : Token {
    override val type: TokenType = TokenType.SYMBOL
    override var value: String = ""

    override fun isIt(string: String): Boolean {
        value = string
        return false
    }

    override fun isSymbolIt(symbol: Char): Boolean = value[0] == symbol

    override fun isTerm(): Boolean = isSymbolIt('(') || isUnaryOp()

    override fun isUnaryOp(): Boolean = "-~".contains(value[0])

    override fun isOp(): Boolean = "+-*/&|<>=".contains(value[0])
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
