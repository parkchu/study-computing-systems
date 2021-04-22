package symbol

class SymbolTable {

    private val table = mutableMapOf<String, Int>()

    init {
        add("SP", 0)
        add("LCL", 1)
        add("ARG", 2)
        add("THIS", 3)
        add("THAT", 4)
        repeat(16) { add("R$it", it) }
        add("SCREEN", 16384)
        add("KBD", 24576)
    }

    fun add(symbol: String, address: Int) {
        table[symbol] = address
    }

    fun contains(symbol: String): Boolean {
        return table.contains(symbol)
    }

    fun getAddress(symbol: String): Int {
        return table[symbol] ?: throw RuntimeException("해당 기호는 추가되지 않았습니다.")
    }
}
