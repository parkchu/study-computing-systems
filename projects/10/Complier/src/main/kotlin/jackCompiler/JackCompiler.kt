package jackCompiler

import compilationEngine.CompilationEngine
import jackTokenizer.JackTokenizer
import java.io.File

class JackCompiler(file: File) {
    private val outputFile = File("${file.parent}/${file.nameWithoutExtension}.vm")
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
