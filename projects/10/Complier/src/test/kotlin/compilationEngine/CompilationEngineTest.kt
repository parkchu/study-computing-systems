package compilationEngine

import jackTokenizer.JackTokenizer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CompilationEngineTest {
    @Test
    fun compileClass() {
        val strings = listOf("class Test {", "}")
        val jackTokenizer = JackTokenizer(strings)
        val compilationEngine = CompilationEngine(jackTokenizer)

        val result = compilationEngine.compileClass()

        val strings2 = listOf(
            "<class>",
            writeKeyword("class"),
            writeIdentifier("Test"),
            writeSymbol("{"),
            writeSymbol("}"),
            "</class>"
        )
        assertThat(result).isEqualTo(strings2)
    }

    @Test
    fun compileClassVarDec() {
        val strings = listOf(
            "class Test {",
            "static boolean test1;",
            "field boolean test2;",
            "}")
        val jackTokenizer = JackTokenizer(strings)
        val compilationEngine = CompilationEngine(jackTokenizer)

        val result = compilationEngine.compileClass()

        val strings2 = listOf(
            "<class>",
            writeKeyword("class"),
            writeIdentifier("Test"),
            writeSymbol("{"),
            "<classVarDec>",
            writeKeyword("static"),
            writeKeyword("boolean"),
            writeIdentifier("test1"),
            writeSymbol(";"),
            "</classVarDec>",
            "<classVarDec>",
            writeKeyword("field"),
            writeKeyword("boolean"),
            writeIdentifier("test2"),
            writeSymbol(";"),
            "</classVarDec>",
            writeSymbol("}"),
            "</class>"
        )
        assertThat(result).isEqualTo(strings2)
    }

    @Test
    fun compileSubroutine() {
        val strings = listOf(
            "class Test {",
            "constructor Test test1() {",
            "}",
            "method void test2() {",
            "}",
            "function void test3() {",
            "}",
            "}")
        val jackTokenizer = JackTokenizer(strings)
        val compilationEngine = CompilationEngine(jackTokenizer)

        val result = compilationEngine.compileClass()

        val strings2 = listOf(
            "<class>",
            writeKeyword("class"),
            writeIdentifier("Test"),
            writeSymbol("{"),
            "<subroutineDec>",
            writeKeyword("constructor"),
            writeIdentifier("Test"),
            writeIdentifier("test1"),
            writeSymbol("("),
            "<parameterList>",
            "</parameterList>",
            writeSymbol(")"),
            "<subroutineBody>",
            writeSymbol("{"),
            writeSymbol("}"),
            "</subroutineBody>",
            "</subroutineDec>",
            "<subroutineDec>",
            writeKeyword("method"),
            writeKeyword("void"),
            writeIdentifier("test2"),
            writeSymbol("("),
            "<parameterList>",
            "</parameterList>",
            writeSymbol(")"),
            "<subroutineBody>",
            writeSymbol("{"),
            writeSymbol("}"),
            "</subroutineBody>",
            "</subroutineDec>",
            "<subroutineDec>",
            writeKeyword("function"),
            writeKeyword("void"),
            writeIdentifier("test3"),
            writeSymbol("("),
            "<parameterList>",
            "</parameterList>",
            writeSymbol(")"),
            "<subroutineBody>",
            writeSymbol("{"),
            writeSymbol("}"),
            "</subroutineBody>",
            "</subroutineDec>",
            writeSymbol("}"),
            "</class>"
        )
        assertThat(result).isEqualTo(strings2)
    }

    private fun writeKeyword(value: String): String = "<keyword> $value </keyword>"

    private fun writeIdentifier(value: String): String = "<identifier> $value </identifier>"

    private fun writeSymbol(value: String): String = "<symbol> $value </symbol>"

    private fun writeIntVal(value: String): String = "<integerConstant> $value </integerConstant>"

    private fun writeStringVal(value: String): String = "<stringConstant> $value </stringConstant>"
}
