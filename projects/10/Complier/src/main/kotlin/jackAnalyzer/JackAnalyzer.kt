package jackAnalyzer

import compilationEngine.CompilationEngine
import jackTokenizer.JackTokenizer
import java.io.File

class JackAnalyzer(file: File) {
    private val outputFile = File("${file.parent}/myCompiler/${file.nameWithoutExtension}.xml")
    private val jackTokenizer = JackTokenizer(file.readLines().map { it.trim() })

    fun write() {
        val compilationEngine = CompilationEngine(jackTokenizer)
        val codes = compilationEngine.compileClass()
        outputFile.bufferedWriter().use { writer ->
            codes.forEach {
                writer.write(it)
                writer.newLine()
            }
        }
    }
}
