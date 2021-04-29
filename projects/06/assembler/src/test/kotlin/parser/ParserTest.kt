package parser

import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.io.File
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ParserTest {
    @Test
    fun `다음 명령어 여부`() {
        val file = TEST_ASM_FILE_A
        val parser = Parser(file)

        val result = parser.hasMoreCommands()

        assertTrue(result)
    }

    @Test
    fun `다음 명령어로 넘기기`() {
        val file = TEST_ASM_FILE_A
        val parser = Parser(file)
        parser.advance()

        val result = parser.hasMoreCommands()

        assertFalse(result)
    }

    @Test
    fun `현재 명령어의 타입 조회`() {
        val file = TEST_ASM_FILE_A
        val parser = Parser(file)
        parser.advance()

        val commandType = parser.getCommandType()

        assertSame(commandType, CommandType.A_COMMAND)
    }

    @Test
    fun `현재 명령어의 기호 얻기`() {
        val fileA = TEST_ASM_FILE_A
        val fileL = TEST_ASM_FILE_L
        val parserA = Parser(fileA)
        val parserL = Parser(fileL)
        parserA.advance()
        parserL.advance()

        val symbolA = parserA.getSymbol()
        val symbolL = parserL.getSymbol()

        assertTrue(symbolA == "100")
        assertTrue(symbolL == "LOOP")
    }

    @Test
    fun `계산 명령어 일때 값 얻기`() {
        val file = TEST_ASM_FILE_C
        val parser = Parser(file)
        parser.advance()

        val comp = parser.getComp()
        val dest = parser.getDest()
        val jump = parser.getJump()

        assertTrue(comp == "D+M")
        assertTrue(dest == "D")
        assertNull(jump)

        parser.advance()

        val comp2 = parser.getComp()
        val dest2 = parser.getDest()
        val jump2 = parser.getJump()

        assertTrue(comp2 == "D")
        assertNull(dest2)
        assertTrue(jump2 == "JMP")
    }

    companion object {
        val TEST_ASM_FILE_A: File = ClassPathResource("asm/a-command.asm").file
        val TEST_ASM_FILE_C: File = ClassPathResource("asm/c-command.asm").file
        val TEST_ASM_FILE_L: File = ClassPathResource("asm/l-command.asm").file
    }
}
