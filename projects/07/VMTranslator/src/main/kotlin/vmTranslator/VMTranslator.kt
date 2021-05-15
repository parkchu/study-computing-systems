package vmTranslator

import codeWriter.CodeWriter
import parser.CommandType
import parser.Parser
import java.io.File

object VMTranslator {
    fun translator(files: List<File>, fileName: String) {
        val codeWriter = CodeWriter("${files.first().parent}/$fileName.asm")
        codeWriter.writeInit()
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
        if (commandType == CommandType.C_LABEL) {
            codeWriter.writeLabel(parser.getArg1())
        }
        if (commandType == CommandType.C_GOTO) {
            codeWriter.writeGoto(parser.getArg1())
        }
        if (commandType == CommandType.C_IF) {
            codeWriter.writeIf(parser.getArg1())
        }
        if (commandType == CommandType.C_CALL) {
            codeWriter.writeCall(parser.getArg1(), parser.getArg2())
        }
        if (commandType == CommandType.C_FUNCTION) {
            codeWriter.writeFunction(parser.getArg1(), parser.getArg2())
        }
        if (commandType == CommandType.C_RETURN) {
            codeWriter.writeReturn()
        }
    }
}
