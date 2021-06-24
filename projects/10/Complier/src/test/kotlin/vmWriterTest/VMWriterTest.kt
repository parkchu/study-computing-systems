package vmWriterTest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import vmWriter.Command
import vmWriter.Segment
import vmWriter.VMWriter

class VMWriterTest {

    @Test
    fun writePush() {
        val vmWriter = VMWriter()

        vmWriter.writePush(Segment.ARG, 0)

        val result = vmWriter.codes[0]
        assertThat(result).isEqualTo("push argument 0")
    }

    @Test
    fun writePop() {
        val vmWriter = VMWriter()

        vmWriter.writePop(Segment.ARG, 0)

        val result = vmWriter.codes[0]
        assertThat(result).isEqualTo("pop argument 0")
    }

    @Test
    fun writeArithmetic() {
        val vmWriter = VMWriter()

        vmWriter.writeArithmetic(Command.ADD)

        val result = vmWriter.codes[0]
        assertThat(result).isEqualTo("add")
    }

    @Test
    fun writeLabel() {
        val vmWriter = VMWriter()

        vmWriter.writeLabel("test")

        val result = vmWriter.codes[0]
        assertThat(result).isEqualTo("label test")
    }

    @Test
    fun writeGoto() {
        val vmWriter = VMWriter()

        vmWriter.writeGoto("test")

        val result = vmWriter.codes[0]
        assertThat(result).isEqualTo("goto test")
    }

    @Test
    fun writeIf() {
        val vmWriter = VMWriter()

        vmWriter.writeIf("test")

        val result = vmWriter.codes[0]
        assertThat(result).isEqualTo("if-goto test")
    }

    @Test
    fun writeCall() {
        val vmWriter = VMWriter()

        vmWriter.writeCall("test", 0)

        val result = vmWriter.codes[0]
        assertThat(result).isEqualTo("call test 0")
    }

    @Test
    fun writeFunction() {
        val vmWriter = VMWriter()

        vmWriter.writeFunction("test", 0)

        val result = vmWriter.codes[0]
        assertThat(result).isEqualTo("function test 0")
    }

    @Test
    fun writeReturn() {
        val vmWriter = VMWriter()

        vmWriter.writeReturn()

        val result = vmWriter.codes[0]
        assertThat(result).isEqualTo("return")
    }
}
