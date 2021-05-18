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
        return CommandType.findByCommand(getCommand())
    }

    private fun getCommand(): String = commandWords.first()

    fun getArg1(): String {
        val commandType = getCommandType()
        if (commandType == CommandType.C_RETURN) {
            return ""
        }
        if (commandType == CommandType.C_ARITHMETIC) {
            return commandWords.first()
        }
        require(commandWords[1].isNotEmpty()) { throw RuntimeException("잘못된 명령어 문장이 있습니다.") }
        return commandWords[1]
    }

    fun getArg2(): String {
        val commandTypes = listOf(CommandType.C_PUSH, CommandType.C_POP, CommandType.C_FUNCTION, CommandType.C_CALL)
        val commandType = getCommandType()
        require(commandTypes.contains(commandType)) { return "" }
        return commandWords[2]
    }
}
