package vmWriter

class VMWriter {
    private val _codes = mutableListOf<String>()
    val codes: List<String>
        get() = _codes

    fun writePush(segment: Segment, index: Int) {
        _codes.add("push ${segment.value} $index")
    }

    fun writePop(segment: Segment, index: Int) {
        _codes.add("pop ${segment.value} $index")
    }

    fun writeArithmetic(command: Command) {
        _codes.add(command.value)
    }

    fun writeLabel(label: String) {
        _codes.add("label $label")
    }

    fun writeGoto(label: String) {
        _codes.add("goto $label")
    }

    fun writeIf(label: String) {
        _codes.add("if-goto $label")
    }

    fun writeCall(name: String, nArgs: Int) {
        _codes.add("call $name $nArgs")
    }

    fun writeFunction(name: String, nLocals: Int) {
        _codes.add("function $name $nLocals")
    }

    fun writeReturn() {
        _codes.add("return")
    }
}
