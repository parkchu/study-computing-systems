import vmTranslator.VMTranslator
import java.io.File

fun main() {
    println("파일 주소를 입력해주십시오")
    val filePath = readLine() ?: throw RuntimeException("파일 주소를 공백으로 입력하실순 없습니다.")
    val file = File(filePath.trim())
    val files = file.walkTopDown().toList().filter { it.extension == "vm" }
    val fileName = if (file.isDirectory) {
        file.name
    } else {
        file.nameWithoutExtension
    }
    VMTranslator.translator(files, fileName)
}
