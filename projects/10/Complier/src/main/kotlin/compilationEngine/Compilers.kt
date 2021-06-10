package compilationEngine

import jackTokenizer.JackTokenizer
import jackTokenizer.Keyword
import jackTokenizer.TokenType

abstract class Compiler(val jackTokenizer: JackTokenizer) {
    val strings = mutableListOf<String>()

    open fun isIt(): Boolean = false

    open fun compile(): List<String> = strings

    fun addMustString(isWrite: Boolean = true) {
        if (isWrite) {
            strings.add(jackTokenizer.getXMLString())
        } else {
            throw RuntimeException(jackTokenizer.getXMLString())
        }
    }

    fun isTerm(): Boolean {
        return jackTokenizer.getTokenType() == TokenType.INT_CONST ||
                jackTokenizer.getTokenType() == TokenType.STRING_CONST ||
                isKeywordIts(listOf(Keyword.TRUE, Keyword.FALSE, Keyword.NULL, Keyword.THIS)) ||
                isIdentifier() || isSymbolIt('(') || isUnaryOp()
    }

    fun isUnaryOp(): Boolean {
        return "-~".contains(jackTokenizer.getSymbol())
    }

    fun isOp(): Boolean {
        return "+-*/&|<>=".contains(jackTokenizer.getSymbol())
    }

    fun isType(): Boolean {
        return isKeywordIts(listOf(Keyword.INT, Keyword.CHAR, Keyword.BOOLEAN)) || isIdentifier()
    }

    fun isKeywordIt(keyword: Keyword): Boolean {
        return jackTokenizer.getTokenType() == TokenType.KEYWORD && jackTokenizer.getKeyword() == keyword
    }

    fun isKeywordIts(keywords: List<Keyword>): Boolean {
        return jackTokenizer.getTokenType() == TokenType.KEYWORD && keywords.contains(jackTokenizer.getKeyword())
    }

    fun isIdentifier(): Boolean {
        return jackTokenizer.getTokenType() == TokenType.IDENTIFIER
    }

    fun isSymbolIt(symbol: Char): Boolean {
        return jackTokenizer.getTokenType() == TokenType.SYMBOL && jackTokenizer.getSymbol() == symbol
    }

    fun compileStatements() {
        strings.add("<statements>")
        while (!isSymbolIt('}')) {
            val compilers = listOf(
                CompilerLet(jackTokenizer),
                CompilerIf(jackTokenizer),
                CompilerWhile(jackTokenizer),
                CompilerDo(jackTokenizer),
                CompilerReturn(jackTokenizer)
            )
            val compiler = compilers.find { it.isIt() } ?: throw RuntimeException("")
            strings.addAll(compiler.compile())
        }
        strings.add("</statements>")
    }
}

class CompilerClassVarDec(jackTokenizer: JackTokenizer) : Compiler(jackTokenizer) {
    override fun isIt(): Boolean = isKeywordIts(listOf(Keyword.STATIC, Keyword.FIELD))

    override fun compile(): List<String> {
        strings.add("<classVarDec>")
        addMustString()
        jackTokenizer.advance()
        addMustString(isType())
        jackTokenizer.advance()
        while (!isSymbolIt(';')) {
            addMustString(isIdentifier() || isSymbolIt(','))
            jackTokenizer.advance()
        }
        addMustString()
        strings.add("</classVarDec>")
        return strings
    }
}

class CompilerSubroutine(jackTokenizer: JackTokenizer) : Compiler(jackTokenizer) {
    override fun isIt(): Boolean = isKeywordIts(listOf(Keyword.CONSTRUCTOR, Keyword.METHOD, Keyword.FUNCTION))

    override fun compile(): List<String> {
        strings.add("<subroutineDec>")
        addMustString()
        jackTokenizer.advance()
        addMustString(isType() || isKeywordIt(Keyword.VOID))
        jackTokenizer.advance()
        addMustString(isIdentifier())
        jackTokenizer.advance()
        addMustString(isSymbolIt('('))
        compileParameterList()
        addMustString(isSymbolIt(')'))
        compileSubroutineBody()
        strings.add("</subroutineDec>")
        return strings
    }

    private fun compileParameterList() {
        strings.add("<parameterList>")
        jackTokenizer.advance()
        while (!isSymbolIt(')')) {
            addMustString(isType() || isSymbolIt(','))
            jackTokenizer.advance()
        }
        strings.add("</parameterList>")
    }

    private fun compileSubroutineBody() {
        strings.add("<subroutineBody>")
        jackTokenizer.advance()
        addMustString(isSymbolIt('{'))
        jackTokenizer.advance()
        while (!isSymbolIt('}')) {
            if (isKeywordIt(Keyword.VAR)) {
                compileVarDec()
            } else {
                compileStatements()
            }
        }
        addMustString(isSymbolIt('}'))
        strings.add("</subroutineBody>")
    }

    private fun compileVarDec() {
        strings.add("<varDec>")
        addMustString()
        jackTokenizer.advance()
        addMustString(isType())
        jackTokenizer.advance()
        while (!isSymbolIt(';')) {
            addMustString(isIdentifier() || isSymbolIt(','))
            jackTokenizer.advance()
        }
        addMustString()
        strings.add("</varDec>")
        jackTokenizer.advance()
    }
}

abstract class CompilerStatements(jackTokenizer: JackTokenizer) : Compiler(jackTokenizer) {
    fun compileExpression() {
        strings.add("<expression>")
        compileTerm()
        while (isOp()) {
            addMustString(isOp())
            jackTokenizer.advance()
            compileTerm()
        }
        strings.add("</expression>")
    }

    fun compileTerm() {
        strings.add("<term>")
        writeTerm()
        strings.add("</term>")
    }

    private fun writeTerm() {
        addMustString(isTerm())
        val compilers = listOf(
            CompilerTerm(jackTokenizer),
            CompilerTermWhenIdentifier(jackTokenizer),
            CompilerTermWhenSymbol(jackTokenizer),
            CompilerTermWhenUnaryOp(jackTokenizer)
        )
        val compiler = compilers.find { it.isIt() } ?: throw RuntimeException("")
        strings.addAll(compiler.compile())
    }

    fun compileExpressionList() {
        strings.add("<expressionList>")
        jackTokenizer.advance()
        while (!isSymbolIt(')')) {
            compileExpression()
            if (isSymbolIt(',')) {
                addMustString()
                jackTokenizer.advance()
            }
        }
        strings.add("</expressionList>")
    }
}

class CompilerIf(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isKeywordIt(Keyword.IF)

    override fun compile(): List<String> {
        strings.add("<ifStatement>")
        addMustString()
        jackTokenizer.advance()
        addMustString(isSymbolIt('('))
        jackTokenizer.advance()
        compileExpression()
        addMustString(isSymbolIt(')'))
        jackTokenizer.advance()
        addMustString(isSymbolIt('{'))
        while (!isSymbolIt('}')) {
            jackTokenizer.advance()
            compileStatements()
        }
        addMustString()
        jackTokenizer.advance()
        if (isKeywordIt(Keyword.ELSE)) {
            compileElse()
        }
        strings.add("</ifStatement>")
        return strings
    }

    private fun compileElse() {
        addMustString()
        jackTokenizer.advance()
        addMustString(isSymbolIt('{'))
        while (!isSymbolIt('}')) {
            jackTokenizer.advance()
            compileStatements()
        }
        addMustString()
        jackTokenizer.advance()
    }
}

class CompilerLet(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isKeywordIt(Keyword.LET)

    override fun compile(): List<String> {
        strings.add("<letStatement>")
        addMustString()
        jackTokenizer.advance()
        addMustString(isIdentifier())
        jackTokenizer.advance()
        if (isSymbolIt('[')) {
            addMustString()
            jackTokenizer.advance()
            compileExpression()
            addMustString(isSymbolIt(']'))
            jackTokenizer.advance()
        }
        addMustString(isSymbolIt('='))
        jackTokenizer.advance()
        compileExpression()
        addMustString(isSymbolIt(';'))
        strings.add("</letStatement>")
        jackTokenizer.advance()
        return strings
    }
}

class CompilerWhile(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isKeywordIt(Keyword.WHILE)

    override fun compile(): List<String> {
        strings.add("<whileStatement>")
        addMustString()
        jackTokenizer.advance()
        addMustString(isSymbolIt('('))
        jackTokenizer.advance()
        compileExpression()
        addMustString(isSymbolIt(')'))
        jackTokenizer.advance()
        addMustString(isSymbolIt('{'))
        while (!isSymbolIt('}')) {
            jackTokenizer.advance()
            compileStatements()
        }
        addMustString(isSymbolIt('}'))
        strings.add("</whileStatement>")
        jackTokenizer.advance()
        return strings
    }
}

class CompilerDo(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isKeywordIt(Keyword.DO)

    override fun compile(): List<String> {
        strings.add("<doStatement>")
        addMustString()
        jackTokenizer.advance()
        addMustString(isIdentifier())
        jackTokenizer.advance()
        if (isSymbolIt('(')) {
            addMustString()
            compileExpressionList()
            addMustString(isSymbolIt(')'))
            jackTokenizer.advance()
        }
        if (isSymbolIt('.')) {
            addMustString()
            jackTokenizer.advance()
            addMustString(isIdentifier())
            jackTokenizer.advance()
            addMustString(isSymbolIt('('))
            compileExpressionList()
            addMustString(isSymbolIt(')'))
            jackTokenizer.advance()
        }
        addMustString(isSymbolIt(';'))
        jackTokenizer.advance()
        strings.add("</doStatement>")
        return strings
    }
}

class CompilerReturn(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isKeywordIt(Keyword.RETURN)

    override fun compile(): List<String> {
        strings.add("<returnStatement>")
        addMustString()
        jackTokenizer.advance()
        while (!isSymbolIt(';')) {
            compileExpression()
        }
        addMustString(isSymbolIt(';'))
        strings.add("</returnStatement>")
        jackTokenizer.advance()
        return strings
    }
}

class CompilerTerm(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = jackTokenizer.getTokenType() == TokenType.INT_CONST ||
            jackTokenizer.getTokenType() == TokenType.STRING_CONST ||
            isKeywordIts(listOf(Keyword.TRUE, Keyword.FALSE, Keyword.NULL, Keyword.THIS))

    override fun compile(): List<String> {
        jackTokenizer.advance()
        return listOf()
    }
}

class CompilerTermWhenIdentifier(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isIdentifier()

    override fun compile(): List<String> {
        jackTokenizer.advance()
        val compilers = listOf(
            CompilerTermWhenArray(jackTokenizer),
            CompilerTermWhenCall(jackTokenizer),
            CompilerTermWhenClassCall(jackTokenizer)
        )
        strings.addAll(compilers.find { it.isIt() }?.compile() ?: listOf())
        return strings
    }
}

class CompilerTermWhenArray(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isSymbolIt('[')

    override fun compile(): List<String> {
        addMustString()
        jackTokenizer.advance()
        compileExpression()
        addMustString(isSymbolIt(']'))
        jackTokenizer.advance()
        return strings
    }
}

class CompilerTermWhenCall(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isSymbolIt('(')

    override fun compile(): List<String> {
        addMustString()
        compileExpressionList()
        addMustString(isSymbolIt(')'))
        jackTokenizer.advance()
        return strings
    }
}

class CompilerTermWhenClassCall(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isSymbolIt('.')

    override fun compile(): List<String> {
        addMustString()
        jackTokenizer.advance()
        addMustString(isIdentifier())
        jackTokenizer.advance()
        addMustString(isSymbolIt('('))
        compileExpressionList()
        addMustString(isSymbolIt(')'))
        jackTokenizer.advance()
        return strings
    }
}

class CompilerTermWhenSymbol(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isSymbolIt('(')

    override fun compile(): List<String> {
        jackTokenizer.advance()
        compileExpression()
        addMustString(isSymbolIt(')'))
        jackTokenizer.advance()
        return strings
    }
}

class CompilerTermWhenUnaryOp(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = isUnaryOp()

    override fun compile(): List<String> {
        jackTokenizer.advance()
        compileTerm()
        return strings
    }
}
