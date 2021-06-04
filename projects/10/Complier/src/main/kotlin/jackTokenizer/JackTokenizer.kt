package jackTokenizer

class JackTokenizer(code: List<String>) {
    private val tokens = makeTokens(code).toMutableList()
    private var _currentToken: Token? = null
    private val currentToken: Token
        get() = _currentToken ?: throw RuntimeException("토큰이 설정되지 않았습니다.")

    private fun makeTokens(code: List<String>): List<Token> {
        val strings = code.map { it.toList() }
        val helper = Helper()
        strings.forEach {
            val line = removeComment(it.joinToString("")).toList()
            addToken(helper, line)
        }
        val tokens = helper.mainList.filter { it.value.isNotBlank() && !"\"".contains(it.value) }
        return filterComment(tokens)
    }

    private fun removeComment(string: String): String {
        return if (string.contains("//")) {
            val range = string.indexOf("//")..string.lastIndex
            string.removeRange(range)
        } else {
            string
        }
    }

    private fun addToken(helper: Helper, line: List<Char>) {
        line.forEach {
            if ("\"".contains(it) && helper.isNotString) {
                helper.setStringStatus(false)
                helper.setStringSymbol(it.toString())
                return@forEach
            }
            if (helper.isStringSymbol(it)) {
                helper.setStringStatus(true)
                helper.setStringSymbol("")
            }
            helper.makeToken(it)
        }
    }

    private fun filterComment(tokens: List<Token>): List<Token> {
        val mainList = mutableListOf<Token>()
        val commentSymbols = mutableListOf<Token>()
        var isComment = false
        tokens.forEach { token ->
            if ("/*".contains(token.value)) {
                commentSymbols.add(token)
                val symbol = commentSymbols.joinToString("") { it.value }
                if (symbol == "/*") {
                    commentSymbols.clear()
                    isComment = true
                }
                if (symbol == "*/") {
                    commentSymbols.clear()
                    isComment = false
                    return@forEach
                }
            } else if (commentSymbols.isNotEmpty()) {
                if (!isComment) {
                    mainList.addAll(commentSymbols)
                }
                commentSymbols.clear()
            }
            if (!isComment && commentSymbols.isEmpty()) {
                mainList.add(token)
            }
        }
        return mainList
    }

    fun hasMoreTokens(): Boolean = tokens.isNotEmpty()

    fun advance() {
        _currentToken = tokens.first()
        tokens.removeFirst()
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
