package compilationEngine

import jackTokenizer.JackTokenizer
import jackTokenizer.Keyword
import jackTokenizer.TokenType

class CompilationEngine(private val jackTokenizer: JackTokenizer) {
    private val strings = mutableListOf<String>()

    fun compileClass(): List<String> {
        strings.add("<class>")
        jackTokenizer.advance()
        addMustString(isClass())
        jackTokenizer.advance()
        addMustString(isIdentifier())
        jackTokenizer.advance()
        addMustString(isSymbolIt('{'))
        jackTokenizer.advance()
        while (jackTokenizer.hasMoreTokens()) {
            val compilers = listOf(CompilerClassVarDec(jackTokenizer), CompilerSubroutine(jackTokenizer))
            val compiler = compilers.find { it.isIt() } ?: throw RuntimeException("")
            strings.addAll(compiler.compile())
            jackTokenizer.advance()
        }
        addMustString(isSymbolIt('}'))
        strings.add("</class>")
        return strings
    }

    private fun isClass(): Boolean = jackTokenizer.getKeyword() == Keyword.CLASS

    private fun isIdentifier(): Boolean {
        return jackTokenizer.getTokenType() == TokenType.IDENTIFIER
    }

    private fun isSymbolIt(symbol: Char): Boolean {
        return jackTokenizer.getTokenType() == TokenType.SYMBOL && jackTokenizer.getSymbol() == symbol
    }

    private fun addMustString(isWrite: Boolean = true) {
        if (isWrite) {
            strings.add(jackTokenizer.getXMLString())
        } else {
            throw RuntimeException(jackTokenizer.getXMLString())
        }
    }
}
