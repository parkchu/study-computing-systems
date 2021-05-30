package jackAnalyzer

import jackTokenizer.JackTokenizer
import java.io.File

class JackAnalyzer(file: File) {
    private val outputFile = File("${file.parent}/myCompiler/${file.nameWithoutExtension}T.xml")
    private val jackTokenizer = JackTokenizer(file.readLines().map { it.trim() })

    fun write() {
        val strings = mutableListOf("<tokens>")
        jackTokenizer.tokens.forEach {
            val type = it.type.value
            strings.add("<$type> ${it.value} </$type>")
        }
        strings.add("</tokens>")
        outputFile.bufferedWriter().use { writer ->
            strings.forEach {
                writer.write(it)
                writer.newLine()
            }
        }
    }
}