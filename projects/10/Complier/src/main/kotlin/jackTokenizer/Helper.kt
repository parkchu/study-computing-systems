package jackTokenizer

class Helper {
    val mainList = mutableListOf<Token>()
    var isNotString = true
        private set
    private val subList = mutableListOf<Char>()
    private var stringSymbol = ""

    fun setStringStatus(boolean: Boolean) {
        isNotString = boolean
    }

    fun setStringSymbol(symbol: String) {
        stringSymbol = symbol
    }

    fun isStringSymbol(symbol: Char): Boolean = stringSymbol.contains(symbol)

    fun makeToken(char: Char) {
        if (isAddToken(char)) {
            val string = subList.joinToString("")
            subList.clear()
            val token = when {
                Keyword.containsIt(string) -> {
                    Token(TokenType.KEYWORD, string)
                }
                "\"".contains(char) -> {
                    Token(TokenType.STRING_CONST, string)
                }
                else -> {
                    val type = try {
                        string[0].toString().toInt()
                        TokenType.INT_CONST
                    } catch (e: Exception) {
                        TokenType.IDENTIFIER
                    }
                    Token(type, string)
                }
            }
            mainList.add(token)
            if (symbols.contains(char)) {
                val symbols = mapOf('<' to "&lt;", '>' to "&gt;", '&' to "&amp;")
                val symbol = symbols[char] ?: char.toString()
                mainList.add(Token(TokenType.SYMBOL, symbol))
            }
        } else {
            subList.add(char)
        }
    }

    private fun isAddToken(char: Char): Boolean = "$symbols ".contains(char) && isNotString

    companion object {
        const val symbols = "{}()[].,;+-*/&|<>=~\""
    }
}