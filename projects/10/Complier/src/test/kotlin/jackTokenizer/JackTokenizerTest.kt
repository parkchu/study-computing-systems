package jackTokenizer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JackTokenizerTest {
    @Test
    fun makeKeywordToken() {
        val strings = listOf("class;")

        val jackTokenizer = JackTokenizer(strings)

        jackTokenizer.advance()
        assertThat(jackTokenizer.getTokenType()).isEqualTo(TokenType.KEYWORD)
        assertThat(jackTokenizer.getKeyword()).isEqualTo(Keyword.CLASS)
    }

    @Test
    fun makeSymbolToken() {
        val strings = listOf("+")

        val jackTokenizer = JackTokenizer(strings)

        jackTokenizer.advance()
        assertThat(jackTokenizer.getTokenType()).isEqualTo(TokenType.SYMBOL)
        assertThat(jackTokenizer.getSymbol()).isEqualTo('+')
    }

    @Test
    fun makeIdentifier() {
        val strings = listOf("test;")

        val jackTokenizer = JackTokenizer(strings)

        jackTokenizer.advance()
        assertThat(jackTokenizer.getTokenType()).isEqualTo(TokenType.IDENTIFIER)
        assertThat(jackTokenizer.getIdentifier()).isEqualTo("test")
    }

    @Test
    fun makeStringConstToken() {
        val strings = listOf("\"test string\";")

        val jackTokenizer = JackTokenizer(strings)

        jackTokenizer.advance()
        assertThat(jackTokenizer.getTokenType()).isEqualTo(TokenType.STRING_CONST)
        assertThat(jackTokenizer.getStringVal()).isEqualTo("test string")
    }

    @Test
    fun makeIntConstToken() {
        val strings = listOf("1;")

        val jackTokenizer = JackTokenizer(strings)

        jackTokenizer.advance()
        assertThat(jackTokenizer.getTokenType()).isEqualTo(TokenType.INT_CONST)
        assertThat(jackTokenizer.getIntVal()).isEqualTo(1)
    }

    @Test
    fun deleteCommentLine() {
        val strings = listOf("// 이건 주석 테스트 입니다", "class;")

        val jackTokenizer = JackTokenizer(strings)

        jackTokenizer.advance()
        assertThat(jackTokenizer.getTokenType()).isEqualTo(TokenType.KEYWORD)
        assertThat(jackTokenizer.getKeyword()).isEqualTo(Keyword.CLASS)
    }

    @Test
    fun deleteCommentOneStar() {
        val strings = listOf("/* 여기부터 주석 시작", "여기서 주석 끝 */", "class;")

        val jackTokenizer = JackTokenizer(strings)

        jackTokenizer.advance()
        assertThat(jackTokenizer.getTokenType()).isEqualTo(TokenType.KEYWORD)
        assertThat(jackTokenizer.getKeyword()).isEqualTo(Keyword.CLASS)
    }

    @Test
    fun deleteCommentTwoStar() {
        val strings = listOf("/** 여기부터 주석 시작", "여기서 주석 끝 */", "class;")

        val jackTokenizer = JackTokenizer(strings)

        jackTokenizer.advance()
        assertThat(jackTokenizer.getTokenType()).isEqualTo(TokenType.KEYWORD)
        assertThat(jackTokenizer.getKeyword()).isEqualTo(Keyword.CLASS)
    }
}
