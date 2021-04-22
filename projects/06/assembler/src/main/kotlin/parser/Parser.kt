package parser

import java.io.File

class Parser(file: File) {
    private var currentCommand: String = ""
    private val commands: MutableList<String> = changeFileToStringList(file).toMutableList()
    private val step2Commands: MutableList<String> = mutableListOf()

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

    fun changeStep2() {
        commands.addAll(step2Commands)
    }

    fun hasMoreCommands(): Boolean = commands.isNotEmpty()

    fun advance() {
        currentCommand = commands.first()
        val commandType = getCommandType()
        if (commandType == CommandType.A_COMMAND || commandType == CommandType.C_COMMAND) {
            step2Commands.add(currentCommand)
        }
        commands.removeFirst()
    }

    fun getCommandType(): CommandType {
        return when {
            currentCommand[0].toString() == "@" -> CommandType.A_COMMAND

            currentCommand[0].toString() == "(" && currentCommand.last().toString() == ")" -> CommandType.L_COMMAND

            else -> CommandType.C_COMMAND
        }
    }

    fun getSymbol(): String {
        val commandType = getCommandType()
        if (commandType == CommandType.A_COMMAND) {
            return currentCommand.removeRange(stringFirstRange)
        }

        if (commandType == CommandType.L_COMMAND) {
            val command = currentCommand.removeRange(stringFirstRange)
            val stringLastRange = command.lastIndex..command.lastIndex
            return command.removeRange(stringLastRange)
        }
        throw RuntimeException("해당 명령어 타입은 지원하지 않습니다.")
    }

    fun getDest(): String? {
        require(getCommandType() == CommandType.C_COMMAND) { "해당 명령어 타입은 지원하지 않습니다." }
        return if (currentCommand.contains("=")) {
            val range = currentCommand.indexOf("=")..currentCommand.lastIndex
            currentCommand.removeRange(range)
        } else {
            null
        }
    }

    fun getComp(): String {
        require(getCommandType() == CommandType.C_COMMAND) { "해당 명령어 타입은 지원하지 않습니다." }
        return if (currentCommand.contains("=") && currentCommand.contains(";")) {
            val range1 = 0..currentCommand.indexOf("=")
            val range2 = currentCommand.indexOf(";")..currentCommand.lastIndex
            currentCommand.removeRange(range1).removeRange(range2)
        } else if (currentCommand.contains("=")) {
            val range = 0..currentCommand.indexOf("=")
            currentCommand.removeRange(range)
        } else if (currentCommand.contains(";")) {
            val range = currentCommand.indexOf(";")..currentCommand.lastIndex
            currentCommand.removeRange(range)
        } else {
            throw RuntimeException("해당 명령어를 읽을 수 없습니다.")
        }
    }

    fun getJump(): String? {
        require(getCommandType() == CommandType.C_COMMAND) { "해당 명령어 타입은 지원하지 않습니다." }
        return if (currentCommand.contains(";")) {
            val range = 0..currentCommand.indexOf(";")
            currentCommand.removeRange(range)
        } else {
            null
        }
    }

    companion object {
        val stringFirstRange = 0..0
    }
}
