package jackTokenizer

class JackTokenizer(code: List<String>) {
    private val tokens = TokensTool().make(code)
    private var _currentToken: Token? = null
    private val currentToken: Token
        get() = _currentToken ?: throw RuntimeException("토큰이 설정되지 않았습니다.")

    fun hasMoreTokens(): Boolean = tokens.isNotEmpty()

    fun advance() {
        _currentToken = tokens.removeFirst()
    }

    fun getTokenType(): TokenType = currentToken.type

    fun getKeyword(): Keyword = Keyword.find(currentToken.value)

    fun getSymbol(): Char = currentToken.value[0]

    fun getIdentifier(): String = currentToken.value

    fun getIntVal(): Int = currentToken.value.toInt()

    fun getStringVal(): String = currentToken.value

    fun getXMLString(): String {
        val tokenType = getTokenType().value
        return "<$tokenType> ${currentToken.value} </$tokenType>"
    }
}
