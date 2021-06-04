package compilationEngine

import jackTokenizer.JackTokenizer
import jackTokenizer.Keyword
import jackTokenizer.TokenType

class CompilationEngine(private val jackTokenizer: JackTokenizer) {
    private val strings = mutableListOf<String>()

    fun compileClass(): List<String> {
        strings.add("<class>")
        jackTokenizer.advance()
        addMustString(isKeywordIt(Keyword.CLASS))
        jackTokenizer.advance()
        addMustString(isIdentifier())
        jackTokenizer.advance()
        addMustString(isSymbolIt('{'))
        jackTokenizer.advance()
        while (jackTokenizer.hasMoreTokens()) {
            when {
                isKeywordIts(listOf(Keyword.STATIC, Keyword.FIELD)) -> {
                    compileClassVarDec()
                }
                isKeywordIts(listOf(Keyword.CONSTRUCTOR, Keyword.METHOD, Keyword.FUNCTION)) -> {
                    compileSubroutine()
                }
                else -> {
                    throw RuntimeException("")
                }
            }
            jackTokenizer.advance()
        }
        addMustString(isSymbolIt('}'))
        strings.add("</class>")
        return strings
    }

    private fun compileClassVarDec() {
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
    }

    private fun compileSubroutine() {
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

    private fun compileStatements() {
        strings.add("<statements>")
        while (!isSymbolIt('}')) {
            when {
                isKeywordIt(Keyword.LET) -> {
                    compileLet()
                }
                isKeywordIt(Keyword.IF) -> {
                    compileIf()
                }
                isKeywordIt(Keyword.WHILE) -> {
                    compileWhile()
                }
                isKeywordIt(Keyword.DO) -> {
                    compileDo()
                }
                isKeywordIt(Keyword.RETURN) -> {
                    compileReturn()
                }
                else -> {
                    throw RuntimeException(jackTokenizer.getXMLString())
                }
            }
        }
        strings.add("</statements>")
    }

    private fun compileLet() {
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
    }

    private fun compileIf() {
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

    private fun compileWhile() {
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
    }

    private fun compileDo() {
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
    }

    private fun compileReturn() {
        strings.add("<returnStatement>")
        addMustString()
        jackTokenizer.advance()
        while (!isSymbolIt(';')) {
            compileExpression()
        }
        addMustString(isSymbolIt(';'))
        strings.add("</returnStatement>")
        jackTokenizer.advance()
    }

    private fun compileExpression() {
        strings.add("<expression>")
        compileTerm()
        while (isOp()) {
            addMustString(isOp())
            jackTokenizer.advance()
            compileTerm()
        }
        strings.add("</expression>")
    }

    private fun compileTerm() {
        strings.add("<term>")
        writeTerm()
        strings.add("</term>")
    }

    private fun writeTerm() {
        addMustString(isTerm())
        if (jackTokenizer.getTokenType() == TokenType.INT_CONST ||
            jackTokenizer.getTokenType() == TokenType.STRING_CONST ||
            isKeywordIts(listOf(Keyword.TRUE, Keyword.FALSE, Keyword.NULL, Keyword.THIS))) {
            jackTokenizer.advance()
        } else if (isIdentifier()) {
            jackTokenizer.advance()
            when {
                isSymbolIt('[') -> {
                    addMustString()
                    jackTokenizer.advance()
                    compileExpression()
                    addMustString(isSymbolIt(']'))
                    jackTokenizer.advance()
                }
                isSymbolIt('(') -> {
                    addMustString()
                    compileExpressionList()
                    addMustString(isSymbolIt(')'))
                    jackTokenizer.advance()
                }
                isSymbolIt('.') -> {
                    addMustString()
                    jackTokenizer.advance()
                    addMustString(isIdentifier())
                    jackTokenizer.advance()
                    addMustString(isSymbolIt('('))
                    compileExpressionList()
                    addMustString(isSymbolIt(')'))
                    jackTokenizer.advance()
                }
            }
        } else if (isSymbolIt('(')) {
            jackTokenizer.advance()
            compileExpression()
            addMustString(isSymbolIt(')'))
            jackTokenizer.advance()
        } else if (isUnaryOp()) {
            jackTokenizer.advance()
            compileTerm()
        }
    }

    private fun compileExpressionList() {
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

    private fun isTerm(): Boolean {
        return jackTokenizer.getTokenType() == TokenType.INT_CONST ||
                jackTokenizer.getTokenType() == TokenType.STRING_CONST ||
                isKeywordIts(listOf(Keyword.TRUE, Keyword.FALSE, Keyword.NULL, Keyword.THIS)) ||
                isIdentifier() || isSymbolIt('(') || isUnaryOp()
    }

    private fun isUnaryOp(): Boolean {
        return "-~".contains(jackTokenizer.getSymbol())
    }

    private fun isOp(): Boolean {
        return "+-*/&|<>=".contains(jackTokenizer.getSymbol())
    }

    private fun isType(): Boolean {
        return isKeywordIts(listOf(Keyword.INT, Keyword.CHAR, Keyword.BOOLEAN)) || isIdentifier()
    }

    private fun isKeywordIt(keyword: Keyword): Boolean {
        return jackTokenizer.getTokenType() == TokenType.KEYWORD && jackTokenizer.getKeyword() == keyword
    }

    private fun isKeywordIts(keywords: List<Keyword>): Boolean {
        return jackTokenizer.getTokenType() == TokenType.KEYWORD && keywords.contains(jackTokenizer.getKeyword())
    }

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