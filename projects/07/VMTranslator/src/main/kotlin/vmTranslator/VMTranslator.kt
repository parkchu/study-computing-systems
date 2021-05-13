package vmTranslator

import codeWriter.CodeWriter
import parser.CommandType
import parser.Parser
import java.io.File

object VMTranslator {
    fun translator(files: List<File>) {
        val filePath = "${files.first().parent}/main.asm"
        val codeWriter = CodeWriter(filePath)
        files.forEach {
            val parser = Parser(it)
            codeWriter.setFileName(it.nameWithoutExtension)
            while (parser.hasMoreCommands()) {
                parser.advance()
                writeFile(parser, codeWriter)
            }
        }
        codeWriter.close()
    }

    private fun writeFile(parser: Parser, codeWriter: CodeWriter) {
        val commandType = parser.getCommandType()
        if (commandType == CommandType.C_ARITHMETIC) {
            codeWriter.writeArithmetic(parser.getCommand())
        }
        if (commandType == CommandType.C_PUSH) {
            codeWriter.writePush(parser.getArg1(), parser.getArg2())
        }
        if (commandType == CommandType.C_POP) {
            codeWriter.writePop(parser.getArg1(), parser.getArg2())
        }
    }
}
