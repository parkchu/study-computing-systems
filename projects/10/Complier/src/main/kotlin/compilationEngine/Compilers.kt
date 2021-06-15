package compilationEngine

import jackTokenizer.JackTokenizer
import jackTokenizer.Keyword
import jackTokenizer.Token
import jackTokenizer.TokenType

abstract class Compiler(val jackTokenizer: JackTokenizer) {
    val codes = mutableListOf<String>()
    val currentToken: Token
        get() = jackTokenizer.currentToken

    open fun isIt(): Boolean = false

    open fun compile(): List<String> = codes

    fun addMustString(isWrite: Boolean = true) {
        if (isWrite) {
            codes.add(jackTokenizer.getXMLString())
        } else {
            throw RuntimeException(jackTokenizer.getXMLString())
        }
    }

    fun compileStatements() {
        codes.add("<statements>")
        while (!currentToken.isSymbolIt('}')) {
            val compilers = listOf(
                CompilerLet(jackTokenizer),
                CompilerIf(jackTokenizer),
                CompilerWhile(jackTokenizer),
                CompilerDo(jackTokenizer),
                CompilerReturn(jackTokenizer)
            )
            val compiler = compilers.find { it.isIt() } ?: throw RuntimeException("")
            codes.addAll(compiler.compile())
        }
        codes.add("</statements>")
    }
}

class CompilerClassVarDec(jackTokenizer: JackTokenizer) : Compiler(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isKeywordIts(listOf(Keyword.STATIC, Keyword.FIELD))

    override fun compile(): List<String> {
        codes.add("<classVarDec>")
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isType())
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt(';')) {
            addMustString(currentToken.isIdentifier() || currentToken.isSymbolIt(','))
            jackTokenizer.advance()
        }
        addMustString()
        codes.add("</classVarDec>")
        return codes
    }
}

class CompilerSubroutine(jackTokenizer: JackTokenizer) : Compiler(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isKeywordIts(listOf(Keyword.CONSTRUCTOR, Keyword.METHOD, Keyword.FUNCTION))

    override fun compile(): List<String> {
        codes.add("<subroutineDec>")
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isType() || currentToken.isKeywordIt(Keyword.VOID))
        jackTokenizer.advance()
        addMustString(currentToken.isIdentifier())
        jackTokenizer.advance()
        addMustString(currentToken.isSymbolIt('('))
        compileParameterList()
        addMustString(currentToken.isSymbolIt(')'))
        compileSubroutineBody()
        codes.add("</subroutineDec>")
        return codes
    }

    private fun compileParameterList() {
        codes.add("<parameterList>")
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt(')')) {
            addMustString(currentToken.isType() || currentToken.isSymbolIt(','))
            jackTokenizer.advance()
        }
        codes.add("</parameterList>")
    }

    private fun compileSubroutineBody() {
        codes.add("<subroutineBody>")
        jackTokenizer.advance()
        addMustString(currentToken.isSymbolIt('{'))
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt('}')) {
            if (currentToken.isKeywordIt(Keyword.VAR)) {
                compileVarDec()
            } else {
                compileStatements()
            }
        }
        addMustString(currentToken.isSymbolIt('}'))
        codes.add("</subroutineBody>")
    }

    private fun compileVarDec() {
        codes.add("<varDec>")
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isType())
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt(';')) {
            addMustString(currentToken.isIdentifier() || currentToken.isSymbolIt(','))
            jackTokenizer.advance()
        }
        addMustString()
        codes.add("</varDec>")
        jackTokenizer.advance()
    }
}

abstract class CompilerStatements(jackTokenizer: JackTokenizer) : Compiler(jackTokenizer) {
    fun compileExpression() {
        codes.add("<expression>")
        compileTerm()
        while (currentToken.isOp()) {
            addMustString(currentToken.isOp())
            jackTokenizer.advance()
            compileTerm()
        }
        codes.add("</expression>")
    }

    fun compileTerm() {
        codes.add("<term>")
        writeTerm()
        codes.add("</term>")
    }

    private fun writeTerm() {
        addMustString(currentToken.isTerm())
        val compilers = listOf(
            CompilerTerm(jackTokenizer),
            CompilerTermWhenIdentifier(jackTokenizer),
            CompilerTermWhenSymbol(jackTokenizer),
            CompilerTermWhenUnaryOp(jackTokenizer)
        )
        val compiler = compilers.find { it.isIt() } ?: throw RuntimeException("")
        codes.addAll(compiler.compile())
    }

    fun compileExpressionList() {
        codes.add("<expressionList>")
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt(')')) {
            compileExpression()
            if (currentToken.isSymbolIt(',')) {
                addMustString()
                jackTokenizer.advance()
            }
        }
        codes.add("</expressionList>")
    }
}

class CompilerIf(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isKeywordIt(Keyword.IF)

    override fun compile(): List<String> {
        codes.add("<ifStatement>")
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isSymbolIt('('))
        jackTokenizer.advance()
        compileExpression()
        addMustString(currentToken.isSymbolIt(')'))
        jackTokenizer.advance()
        addMustString(currentToken.isSymbolIt('{'))
        while (!currentToken.isSymbolIt('}')) {
            jackTokenizer.advance()
            compileStatements()
        }
        addMustString()
        jackTokenizer.advance()
        if (currentToken.isKeywordIt(Keyword.ELSE)) {
            compileElse()
        }
        codes.add("</ifStatement>")
        return codes
    }

    private fun compileElse() {
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isSymbolIt('{'))
        while (!currentToken.isSymbolIt('}')) {
            jackTokenizer.advance()
            compileStatements()
        }
        addMustString()
        jackTokenizer.advance()
    }
}

class CompilerLet(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isKeywordIt(Keyword.LET)

    override fun compile(): List<String> {
        codes.add("<letStatement>")
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isIdentifier())
        jackTokenizer.advance()
        if (currentToken.isSymbolIt('[')) {
            addMustString()
            jackTokenizer.advance()
            compileExpression()
            addMustString(currentToken.isSymbolIt(']'))
            jackTokenizer.advance()
        }
        addMustString(currentToken.isSymbolIt('='))
        jackTokenizer.advance()
        compileExpression()
        addMustString(currentToken.isSymbolIt(';'))
        codes.add("</letStatement>")
        jackTokenizer.advance()
        return codes
    }
}

class CompilerWhile(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isKeywordIt(Keyword.WHILE)

    override fun compile(): List<String> {
        codes.add("<whileStatement>")
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isSymbolIt('('))
        jackTokenizer.advance()
        compileExpression()
        addMustString(currentToken.isSymbolIt(')'))
        jackTokenizer.advance()
        addMustString(currentToken.isSymbolIt('{'))
        while (!currentToken.isSymbolIt('}')) {
            jackTokenizer.advance()
            compileStatements()
        }
        addMustString(currentToken.isSymbolIt('}'))
        codes.add("</whileStatement>")
        jackTokenizer.advance()
        return codes
    }
}

class CompilerDo(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isKeywordIt(Keyword.DO)

    override fun compile(): List<String> {
        codes.add("<doStatement>")
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isIdentifier())
        jackTokenizer.advance()
        if (currentToken.isSymbolIt('(')) {
            addMustString()
            compileExpressionList()
            addMustString(currentToken.isSymbolIt(')'))
            jackTokenizer.advance()
        }
        if (currentToken.isSymbolIt('.')) {
            addMustString()
            jackTokenizer.advance()
            addMustString(currentToken.isIdentifier())
            jackTokenizer.advance()
            addMustString(currentToken.isSymbolIt('('))
            compileExpressionList()
            addMustString(currentToken.isSymbolIt(')'))
            jackTokenizer.advance()
        }
        addMustString(currentToken.isSymbolIt(';'))
        jackTokenizer.advance()
        codes.add("</doStatement>")
        return codes
    }
}

class CompilerReturn(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isKeywordIt(Keyword.RETURN)

    override fun compile(): List<String> {
        codes.add("<returnStatement>")
        addMustString()
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt(';')) {
            compileExpression()
        }
        addMustString(currentToken.isSymbolIt(';'))
        codes.add("</returnStatement>")
        jackTokenizer.advance()
        return codes
    }
}

class CompilerTerm(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = jackTokenizer.getTokenType() == TokenType.INT_CONST ||
            jackTokenizer.getTokenType() == TokenType.STRING_CONST ||
            currentToken.isKeywordIts(listOf(Keyword.TRUE, Keyword.FALSE, Keyword.NULL, Keyword.THIS))

    override fun compile(): List<String> {
        jackTokenizer.advance()
        return listOf()
    }
}

class CompilerTermWhenIdentifier(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isIdentifier()

    override fun compile(): List<String> {
        jackTokenizer.advance()
        val compilers = listOf(
            CompilerTermWhenArray(jackTokenizer),
            CompilerTermWhenCall(jackTokenizer),
            CompilerTermWhenClassCall(jackTokenizer)
        )
        codes.addAll(compilers.find { it.isIt() }?.compile() ?: listOf())
        return codes
    }
}

class CompilerTermWhenArray(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isSymbolIt('[')

    override fun compile(): List<String> {
        addMustString()
        jackTokenizer.advance()
        compileExpression()
        addMustString(currentToken.isSymbolIt(']'))
        jackTokenizer.advance()
        return codes
    }
}

class CompilerTermWhenCall(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isSymbolIt('(')

    override fun compile(): List<String> {
        addMustString()
        compileExpressionList()
        addMustString(currentToken.isSymbolIt(')'))
        jackTokenizer.advance()
        return codes
    }
}

class CompilerTermWhenClassCall(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isSymbolIt('.')

    override fun compile(): List<String> {
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isIdentifier())
        jackTokenizer.advance()
        addMustString(currentToken.isSymbolIt('('))
        compileExpressionList()
        addMustString(currentToken.isSymbolIt(')'))
        jackTokenizer.advance()
        return codes
    }
}

class CompilerTermWhenSymbol(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isSymbolIt('(')

    override fun compile(): List<String> {
        jackTokenizer.advance()
        compileExpression()
        addMustString(currentToken.isSymbolIt(')'))
        jackTokenizer.advance()
        return codes
    }
}

class CompilerTermWhenUnaryOp(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isUnaryOp()

    override fun compile(): List<String> {
        jackTokenizer.advance()
        compileTerm()
        return codes
    }
}
