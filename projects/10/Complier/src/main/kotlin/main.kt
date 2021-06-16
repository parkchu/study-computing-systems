import jackCompiler.JackCompiler
import java.io.File

fun main () {
    println("파일 주소를 입력해주십시오")
    val filePath = readLine() ?: throw RuntimeException("파일 주소를 공백으로 입력하실순 없습니다.")
    println("컴파일 중입니다 잠시만 기다려 주십시오")
    val file = File(filePath.trim())
    val files = file.walkTopDown().toList().filter { it.extension == "jack" }
    files.forEach {
        val jackAnalyzer = JackCompiler(it)
        jackAnalyzer.write()
        println("${it.nameWithoutExtension} : ${it.path}")
    }
    println("컴파일이 완료되었습니다.")
}
