package vmTranslator

import codeWriter.CodeWriter
import parser.CommandType
import parser.Parser
import java.io.File

object VMTranslator {
    fun translator(file: File) {
        val parser = Parser(file)
        val codeWriter = CodeWriter(file)
        while (parser.hasMoreCommands()) {
            parser.advance()
            writeFile(parser, codeWriter)
        }
        codeWriter.close()
    }

    private fun writeFile(parser: Parser, codeWriter: CodeWriter) {
        val commandType = parser.getCommandType()
        if (commandType == CommandType.C_ARITHMETIC) {
            codeWriter.writeArithmetic(parser.getCommand())
        }
        if (commandType == CommandType.C_PUSH || commandType == CommandType.C_POP) {
            codeWriter.writePushPop(commandType, parser.getArg1(), parser.getArg2())
        }
    }
}
