package assembler

import code.Code
import parser.CommandType
import parser.Parser
import java.io.File

object Assembler {
    private const val A_COMMAND_BASIC = "0000000000000000"
    private const val C_COMMAND_FIRST = "111"

    fun assemble(file: File) {
        val parser = Parser(file)
        val commands = mutableListOf<String>()
        while (parser.hasMoreCommands()) {
            parser.advance()
            val result = when(parser.getCommandType()) {
                CommandType.A_COMMAND -> assembleACommand(parser)

                CommandType.C_COMMAND -> assembleCCommand(parser)

                CommandType.L_COMMAND -> parser.getSymbol()
            }
            println(result)
            commands.add(result)
        }
        val hackFileName = file.name.replace(".asm", ".hack")
        val path = file.absolutePath.replace(file.name, hackFileName)
        val hackFile = File(path)
        writeFile(hackFile, commands)
    }

    private fun assembleACommand(parser: Parser): String {
        val address = Integer.toBinaryString(parser.getSymbol().toInt())
        val range = A_COMMAND_BASIC.lastIndex - address.lastIndex..A_COMMAND_BASIC.lastIndex
        return A_COMMAND_BASIC.replaceRange(range, address)
    }

    private fun assembleCCommand(parser: Parser): String {
        val comp = Code.changeCompToBit(parser.getComp())
        val dest = Code.changeDestToBit(parser.getDest())
        val jump = Code.changeJumpToBit(parser.getJump())
        return C_COMMAND_FIRST + comp + dest + jump
    }

    private fun writeFile(file: File, strings: List<String>) {
        file.bufferedWriter().use { writer ->
            strings.forEach {
                writer.write(it)
                writer.newLine()
            }
        }
    }
}
