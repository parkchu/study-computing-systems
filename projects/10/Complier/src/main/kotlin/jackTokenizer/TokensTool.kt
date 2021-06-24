package jackTokenizer

class TokensTool {
    private val mainList = mutableListOf<Token>()
    private val subList = mutableListOf<Char>()
    private var isNotString = true
    private var stringSymbol = ""

    fun make(code: List<String>): MutableList<Token> {
        val strings = code.map { it.toList() }
        strings.forEach {
            val line = removeComment(it.joinToString("")).toList()
            addToken(line)
        }
        val tokens = mainList.filter { it.value.isNotBlank() && !"\"".contains(it.value) }
        return filterComment(tokens).toMutableList()
    }

    private fun removeComment(string: String): String {
        return if (string.contains("//")) {
            val range = string.indexOf("//")..string.lastIndex
            string.removeRange(range)
        } else {
            string
        }
    }

    private fun addToken(line: List<Char>) {
        line.forEach {
            if ("\"".contains(it) && isNotString) {
                isNotString = false
                stringSymbol = it.toString()
                return@forEach
            }
            if (isStringSymbol(it)) {
                isNotString = true
                stringSymbol = ""
            }
            makeToken(it)
        }
    }

    private fun isStringSymbol(symbol: Char): Boolean = stringSymbol.contains(symbol)

    private fun makeToken(char: Char) {
        if (isAddToken(char)) {
            val string = subList.joinToString("")
            subList.clear()
            val tokenFrames = listOf(KeywordToken(), StringValToken(), IdentifierToken(), IntValToken())
            val token = tokenFrames.find { it.isIt(string + char) } ?: throw RuntimeException("")
            mainList.add(token)
            addSymbol(char)
        } else {
            subList.add(char)
        }
    }

    private fun addSymbol(char: Char) {
        if (symbols.contains(char)) {
            val token = SymbolToken()
            val symbol = char.toString()
            token.isIt(symbol)
            mainList.add(token)
        }
    }

    private fun isAddToken(char: Char): Boolean = "$symbols ".contains(char) && isNotString

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

    companion object {
        const val symbols = "{}()[].,;+-*/&|<>=~\""
    }
}
