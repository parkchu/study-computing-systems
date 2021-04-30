package codeWriter

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import parser.ParserTest.Companion.FILE

class CodeWriterTest {
    @Test
    fun setFileName() {
        val codeWriter = CodeWriter(FILE)

        codeWriter.setFileName(FILE.parent, "test")

        assertThat(codeWriter.outputFile?.name).isEqualTo("test.asm")
    }

    @Test
    fun writeArithmetic() {

    }
}
