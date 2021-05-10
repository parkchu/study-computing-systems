package parser

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.core.io.ClassPathResource
import java.io.File

class ParserTest {
    @Test
    fun getCommandType() {
        val parser = Parser(FILE)
        parser.advance()

        val commandType1 = parser.getCommandType()

        assertThat(commandType1).isEqualTo(CommandType.C_PUSH)

        parser.advance()

        val commandType2 = parser.getCommandType()

        assertThat(commandType2).isEqualTo(CommandType.C_ARITHMETIC)
    }

    @Test
    fun getArg1() {
        val parser = Parser(FILE)
        parser.advance()

        val arg1 = parser.getArg1()

        assertThat(arg1).isEqualTo("constant")
    }

    @Test
    fun getArg2() {
        val parser = Parser(FILE)
        parser.advance()

        val arg2 = parser.getArg2()

        assertThat(arg2).isEqualTo(0)
    }

    companion object {
        val FILE: File = ClassPathResource("vm/test.vm").file
    }
}
