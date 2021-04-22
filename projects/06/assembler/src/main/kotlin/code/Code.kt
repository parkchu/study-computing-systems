package code

object Code {
    fun changeDestToBit(destString: String?): String {
        return when(destString) {
            null -> "000"

            "M" -> "001"

            "D" -> "010"

            "MD" -> "011"

            "A" -> "100"

            "AM" -> "101"

            "AD" -> "110"

            "AMD" -> "111"

            else -> throw RuntimeException("명령어 잘못 작성함")
        }
    }

    fun changeCompToBit(compString: String): String {
        val compA = if (compString.contains("M")) {
            "1"
        } else {
            "0"
        }
        val bit =  when(compString.replace("M", "A")) {
            "0" -> "101010"

            "1" -> "111111"

            "-1" -> "111010"

            "D" -> "001100"

            "A" -> "110000"

            "!D" -> "001101"

            "!A" -> "110001"

            "-D" -> "001111"

            "-A" -> "110011"

            "D+1" -> "011111"

            "A+1" -> "110111"

            "D-1" -> "001110"

            "A-1" -> "110010"

            "D+A" -> "000010"

            "D-A" -> "010011"

            "A-D" -> "000111"

            "D&A" -> "000000"

            "D|A" -> "010101"

            else -> throw RuntimeException("명령어 잘못 작성함")
        }
        return compA + bit
    }

    fun changeJumpToBit(jumpString: String?): String {
        return when(jumpString) {
            null -> "000"

            "JGT" -> "001"

            "JEQ" -> "010"

            "JGE" -> "011"

            "JLT" -> "100"

            "JNE" -> "101"

            "JLE" -> "110"

            "JMP" -> "111"

            else -> throw RuntimeException("명령어 잘못 작성함")
        }
    }
}
