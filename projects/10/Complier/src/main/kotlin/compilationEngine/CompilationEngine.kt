package compilationEngine

import jackTokenizer.JackTokenizer
import jackTokenizer.Keyword
import jackTokenizer.Token
import symbolTable.SymbolTable

val symbolTable = SymbolTable()

class CompilationEngine(private val jackTokenizer: JackTokenizer) {
    private val codes = mutableListOf<String>()
    private val currentToken: Token
        get() = jackTokenizer.currentToken

    fun compileClass(): List<String> {
        symbolTable.init()
        jackTokenizer.advance()
        addMustString(currentToken.isKeywordIt(Keyword.CLASS))
        jackTokenizer.advance()
        addMustString(currentToken.isIdentifier())
        symbolTable.setClassName(currentToken.value)
        jackTokenizer.advance()
        addMustString(currentToken.isSymbolIt('{'))
        jackTokenizer.advance()
        while (jackTokenizer.hasMoreTokens()) {
            val compilers = listOf(CompilerClassVarDec(jackTokenizer), CompilerSubroutine(jackTokenizer))
            val compiler = compilers.find { it.isIt() } ?: throw RuntimeException("")
            codes.addAll(compiler.compile())
            jackTokenizer.advance()
        }
        addMustString(currentToken.isSymbolIt('}'))
        return codes
    }

    private fun addMustString(isWrite: Boolean = true) {
        if (isWrite) {
            // codes.add(jackTokenizer.getXMLString())
        } else {
            throw RuntimeException(jackTokenizer.getXMLString())
        }
    }
}
