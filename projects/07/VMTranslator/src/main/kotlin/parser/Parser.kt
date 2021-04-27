package parser

import java.io.File

class Parser(file: File) {
    private val commands: MutableList<String> = changeFileToStringList(file).toMutableList()
    private var currentCommand: String = ""
    private val commandWords: List<String> get() = currentCommand.split(" ")

    private fun changeFileToStringList(file: File): List<String> {
        val commands = file.readLines().map { removeUnnecessaryString(it).trim() }
        return commands.filter { it.isNotBlank() }
    }

    private fun removeUnnecessaryString(string: String): String {
        return if (string.contains("//")) {
            val range = string.indexOf("//")..string.lastIndex
            string.removeRange(range)
        } else {
            string
        }
    }

    fun hasMoreCommands(): Boolean = commands.isNotEmpty()

    fun advance() {
        currentCommand = commands.first()
        commands.removeFirst()
    }

    fun getCommandType(): CommandType {
        val command = commandWords.first()
        return CommandType.findByCommand(command)
    }

    fun getArg1(): String {
        val commandType = getCommandType()
        require(commandType != CommandType.C_RETURN) { throw RuntimeException("해당 명령어는 지원하지 않는 기능입니다.") }
        require(commandWords[1].isNotEmpty()) { throw RuntimeException("잘못된 명령어 문장이 있습니다.") }
        if (commandType == CommandType.C_ARITHMETIC) {
            return commandWords.first()
        }
        return commandWords[1]
    }

    fun getArg2(): Int {
        val commandTypes = listOf(CommandType.C_PUSH, CommandType.C_POP, CommandType.C_FUNCTION, CommandType.C_CALL)
        val commandType = getCommandType()
        require(commandTypes.contains(commandType)) { throw RuntimeException("해당 명령어는 지원하지 않는 기능입니다.") }
        return commandWords[2].toInt()
    }
}
