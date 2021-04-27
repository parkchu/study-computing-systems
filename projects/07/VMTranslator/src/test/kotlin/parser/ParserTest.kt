package parser

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File

class ParserTest {
    @Test
    fun getCommandType() {
        val parser = Parser(FILE)
        parser.advance()

        val commandType = parser.getCommandType()

        assertThat(commandType).isEqualTo(CommandType.C_PUSH)
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
        private val FILE = File("./src/test/kotlin/vm/test.vm")
    }
}
