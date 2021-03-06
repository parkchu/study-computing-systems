import assembler.Assembler
import java.io.File

fun main() {
    println("파일 주소를 입력해주십시오")
    val filePath = readLine() ?: throw RuntimeException("파일 주소를 공백으로 입력하실순 없습니다.")
    val file = File(filePath.trim())
    Assembler.assemble(file)
}
