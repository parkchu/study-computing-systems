package assembler

import code.Code
import parser.CommandType
import parser.Parser
import symbol.SymbolTable
import java.io.File

object Assembler {
    private const val A_COMMAND_BASIC = "0000000000000000"
    private const val C_COMMAND_FIRST = "111"

    private val symbolTable: SymbolTable = SymbolTable()

    private var lCommandAddress: Int = 0
    private var valueAddress: Int = 15

    fun assemble(file: File) {
        val parser = Parser(file)
        val hackFileName = file.name.replace(".asm", ".hack")
        val path = file.absolutePath.replace(file.name, hackFileName)
        val hackFile = File(path)
        step1(parser)
        writeFile(hackFile, step2(parser))
    }

    private fun step1(parser: Parser) {
        while (parser.hasMoreCommands()) {
            parser.advance()
            when(parser.getCommandType()) {
                CommandType.A_COMMAND -> lCommandAddress += 1

                CommandType.C_COMMAND -> lCommandAddress += 1

                CommandType.L_COMMAND -> assembleLCommand(parser)
            }
        }
    }

    private fun step2(parser: Parser): List<String> {
        parser.changeStep2()
        val commands = mutableListOf<String>()
        while (parser.hasMoreCommands()) {
            parser.advance()
            val result = when(parser.getCommandType()) {
                CommandType.A_COMMAND -> assembleACommand(parser)

                CommandType.C_COMMAND -> assembleCCommand(parser)

                else -> throw RuntimeException()
            }
            println(result)
            commands.add(result)
        }
        return commands
    }

    private fun assembleACommand(parser: Parser): String {
        lCommandAddress += 1
        val symbol = parser.getSymbol()
        val int = try {
            symbol.toInt()
        } catch (e: Exception) {
            getSymbolAddress(symbol)
        }
        val address = Integer.toBinaryString(int)
        val range = A_COMMAND_BASIC.lastIndex - address.lastIndex..A_COMMAND_BASIC.lastIndex
        return A_COMMAND_BASIC.replaceRange(range, address)
    }

    private fun getSymbolAddress(symbol: String): Int {
        return if (symbolTable.contains(symbol)) {
            symbolTable.getAddress(symbol)
        } else {
            valueAddress += 1
            symbolTable.add(symbol, valueAddress)
            valueAddress
        }
    }

    private fun assembleCCommand(parser: Parser): String {
        lCommandAddress += 1
        val comp = Code.changeCompToBit(parser.getComp())
        val dest = Code.changeDestToBit(parser.getDest())
        val jump = Code.changeJumpToBit(parser.getJump())
        return C_COMMAND_FIRST + comp + dest + jump
    }

    private fun assembleLCommand(parser: Parser): String{
        val symbol = parser.getSymbol()
        symbolTable.add(symbol, lCommandAddress)
        return ""
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
