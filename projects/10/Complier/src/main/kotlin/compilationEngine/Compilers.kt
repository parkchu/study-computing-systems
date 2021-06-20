package compilationEngine

import jackTokenizer.JackTokenizer
import jackTokenizer.Keyword
import jackTokenizer.Token
import jackTokenizer.TokenType
import symbolTable.Kind

abstract class Compiler(val jackTokenizer: JackTokenizer) {
    val codes = mutableListOf<String>()
    val currentToken: Token
        get() = jackTokenizer.currentToken

    open fun isIt(): Boolean = false

    open fun compile(): List<String> = codes

    fun addMustString(isWrite: Boolean = true) {
        if (isWrite) {
            // codes.add(jackTokenizer.getXMLString())
        } else {
            throw RuntimeException(jackTokenizer.getXMLString())
        }
    }

    fun addMoreInformation(action: String, name: String) {
        // codes.add("<moreInformation> $action ${symbolTable.kindOf(name)} ${symbolTable.indexOf(name)} </moreInformation>")
    }

    fun compileStatements() {
        codes.add("function ${symbolTable.className}.${symbolTable.subroutineName} ${symbolTable.varCount(Kind.VAR)}")
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
    }
}

class CompilerClassVarDec(jackTokenizer: JackTokenizer) : Compiler(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isKeywordIts(listOf(Keyword.STATIC, Keyword.FIELD))

    override fun compile(): List<String> {
        addMustString()
        val kind = Kind.findIt(currentToken.value)
        jackTokenizer.advance()
        addMustString(currentToken.isType())
        if (currentToken.isIdentifier()) {
            codes.add("<moreInformation> use CLASS </moreInformation>")
        }
        val type = currentToken.value
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt(';')) {
            addMustString(currentToken.isIdentifier() || currentToken.isSymbolIt(','))
            if (currentToken.isIdentifier()) {
                val name = currentToken.value
                symbolTable.define(name, type, kind)
                addMoreInformation("define", name)
            }
            jackTokenizer.advance()
        }
        addMustString()
        return codes
    }
}

class CompilerSubroutine(jackTokenizer: JackTokenizer) : Compiler(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isKeywordIts(listOf(Keyword.CONSTRUCTOR, Keyword.METHOD, Keyword.FUNCTION))

    override fun compile(): List<String> {
        symbolTable.startSubroutine()
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isType() || currentToken.isKeywordIt(Keyword.VOID))
        jackTokenizer.advance()
        addMustString(currentToken.isIdentifier())
        symbolTable.setSubroutineName(currentToken.value)
        jackTokenizer.advance()
        addMustString(currentToken.isSymbolIt('('))
        compileParameterList()
        addMustString(currentToken.isSymbolIt(')'))
        compileSubroutineBody()
        return codes
    }

    private fun compileParameterList() {
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt(')')) {
            addMustString(currentToken.isType())
            if (currentToken.isIdentifier()) {
                codes.add("<moreInformation> use CLASS </moreInformation>")
            }
            val type = currentToken.value
            jackTokenizer.advance()
            addMustString(currentToken.isIdentifier())
            val name = currentToken.value
            symbolTable.define(name, type, Kind.ARG)
            addMoreInformation("define", name)
            jackTokenizer.advance()
            if (currentToken.isSymbolIt(',')) {
                addMustString()
                jackTokenizer.advance()
            }
        }
    }

    private fun compileSubroutineBody() {
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
    }

    private fun compileVarDec() {
        codes.add("<varDec>")
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isType())
        if (currentToken.isIdentifier()) {
            codes.add("<moreInformation> use CLASS </moreInformation>")
        }
        val type = currentToken.value
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt(';')) {
            addMustString(currentToken.isIdentifier() || currentToken.isSymbolIt(','))
            if (currentToken.isIdentifier()) {
                val name = currentToken.value
                symbolTable.define(name, type, Kind.VAR)
                addMoreInformation("define", name)
            }
            jackTokenizer.advance()
        }
        addMustString()
        codes.add("</varDec>")
        jackTokenizer.advance()
    }
}

abstract class CompilerStatements(jackTokenizer: JackTokenizer) : Compiler(jackTokenizer) {
    fun compileExpression() {
        compileTerm()
        while (currentToken.isOp()) {
            addMustString(currentToken.isOp())
            val op = currentToken.value
            jackTokenizer.advance()
            compileTerm()
            codes.add(ops[op] ?: throw RuntimeException("잘못된 기호를 입력하였습니다."))
        }
    }

    fun compileTerm() {
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

    fun compileExpressionList(): Int {
        var count = 0
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt(')')) {
            count++
            compileExpression()
            if (currentToken.isSymbolIt(',')) {
                addMustString()
                jackTokenizer.advance()
            }
        }
        return count
    }

    companion object {
        val ops = mapOf("+" to "add", "-" to "sub", "*" to "call Math.multiply 2", "=" to "eq", ">" to "gt", "<" to "lt", "&" to "and", "|" to "or")
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
        addMoreInformation("init", currentToken.value)
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
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isIdentifier())
        val name = currentToken.value
        jackTokenizer.advance()
        if (currentToken.isSymbolIt('(')) {
            codes.add("<moreInformation> use SUBROUTINE </moreInformation>")
            addMustString()
            val index = compileExpressionList() + 1
            addMustString(currentToken.isSymbolIt(')'))
            jackTokenizer.advance()
            codes.add("call ${symbolTable.className}.$name $index")
        }
        if (currentToken.isSymbolIt('.')) {
            val count: Int
            val className = if (symbolTable.kindOf(name) == Kind.NONE) {
                count = 0
                name
            } else {
                count = 1
                symbolTable.typeOf(name)
            }
            addMustString()
            jackTokenizer.advance()
            addMustString(currentToken.isIdentifier())
            val subroutineName = currentToken.value
            jackTokenizer.advance()
            addMustString(currentToken.isSymbolIt('('))
            val index = compileExpressionList() + count
            addMustString(currentToken.isSymbolIt(')'))
            jackTokenizer.advance()
            codes.add("call $className.$subroutineName $index")
        }
        addMustString(currentToken.isSymbolIt(';'))
        jackTokenizer.advance()
        return codes
    }
}

class CompilerReturn(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isKeywordIt(Keyword.RETURN)

    override fun compile(): List<String> {
        addMustString()
        jackTokenizer.advance()
        while (!currentToken.isSymbolIt(';')) {
            compileExpression()
        }
        addMustString(currentToken.isSymbolIt(';'))
        jackTokenizer.advance()
        codes.add("return")
        return codes
    }
}

class CompilerTerm(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = jackTokenizer.getTokenType() == TokenType.INT_CONST ||
            jackTokenizer.getTokenType() == TokenType.STRING_CONST ||
            currentToken.isKeywordIts(listOf(Keyword.TRUE, Keyword.FALSE, Keyword.NULL, Keyword.THIS))

    override fun compile(): List<String> {
        if (currentToken.type == TokenType.INT_CONST) {
            codes.add("push constant ${currentToken.value}")
        }
        jackTokenizer.advance()
        return codes
    }
}

class CompilerTermWhenIdentifier(jackTokenizer: JackTokenizer) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isIdentifier()

    override fun compile(): List<String> {
        val symbolName = currentToken.value
        val compilers = listOf(
            CompilerTermWhenArray(jackTokenizer, symbolName),
            CompilerTermWhenCall(jackTokenizer),
            CompilerTermWhenClassCall(jackTokenizer, symbolName)
        )
        jackTokenizer.advance()
        val codes = compilers.find { it.isIt() }?.compile() ?: listOf()
        if (codes.isEmpty()) {
            addMoreInformation("use", symbolName)
        }
        this.codes.addAll(codes)
        return this.codes
    }
}

class CompilerTermWhenArray(jackTokenizer: JackTokenizer, private val symbolName: String) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isSymbolIt('[')

    override fun compile(): List<String> {
        addMoreInformation("use", symbolName)
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
        codes.add("<moreInformation> use SUBROUTINE </moreInformation>")
        addMustString()
        compileExpressionList()
        addMustString(currentToken.isSymbolIt(')'))
        jackTokenizer.advance()
        return codes
    }
}

class CompilerTermWhenClassCall(jackTokenizer: JackTokenizer, private val symbolName: String) : CompilerStatements(jackTokenizer) {
    override fun isIt(): Boolean = currentToken.isSymbolIt('.')

    override fun compile(): List<String> {
        if (symbolTable.kindOf(symbolName) == Kind.NONE) {
            codes.add("<moreInformation> use CLASS </moreInformation>")
        } else {
            addMoreInformation("use", symbolName)
        }
        addMustString()
        jackTokenizer.advance()
        addMustString(currentToken.isIdentifier())
        codes.add("<moreInformation> use SUBROUTINE </moreInformation>")
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
