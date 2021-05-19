package vmTranslator

import codeWriter.CodeWriter
import commands.*
import parser.Parser
import java.io.File
import java.lang.RuntimeException

object VMTranslator {
    val commands = listOf(
        ArithmeticCommand(),
        PushCommand(),
        PopCommand(),
        LabelCommand(),
        GotoCommand(),
        IfCommand(),
        CallCommand(),
        FunctionCommand(),
        ReturnCommand()
    )

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
        val command = commands.find { it.isSupports(commandType) } ?: throw RuntimeException("지원하지 않는 명령어 입니다.")
        val args = listOf(parser.getArg1(), parser.getArg2())
        command.write(codeWriter, args)
    }
}
