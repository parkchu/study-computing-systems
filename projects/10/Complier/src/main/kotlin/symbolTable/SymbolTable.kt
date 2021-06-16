package symbolTable

class SymbolTable {
    private val classSymbols = mutableListOf<Symbol>()
    private val subroutineSymbols = mutableListOf<Symbol>()
    private val symbols: List<Symbol>
        get() = classSymbols + subroutineSymbols
    private val map = mapOf(
        Kind.STATIC to classSymbols,
        Kind.FIELD to classSymbols,
        Kind.ARG to subroutineSymbols,
        Kind.VAR to subroutineSymbols
    )

    fun startSubroutine() {
        subroutineSymbols.clear()
    }

    fun define(name: String, type: String, kind: Kind) {
        val index = symbols.findLast { it.isKindIt(kind) }?.index ?: -1
        val symbols = map[kind] ?: throw RuntimeException("")
        val symbol = Symbol(name, type, kind, index + 1)
        symbols.add(symbol)
    }

    fun varCount(kind: Kind): Int = symbols.count { it.isKindIt(kind) }

    fun kindOf(name: String): Kind {
        return subroutineSymbols.find { it.isNameIt(name) }?.kind
            ?: classSymbols.find { it.isNameIt(name) }?.kind
            ?: Kind.NONE
    }

    fun typeOf(name: String): String {
        return subroutineSymbols.find { it.isNameIt(name) }?.type
            ?: classSymbols.find { it.isNameIt(name) }?.type
            ?: throw RuntimeException("해당 심볼은 존재하지 않습니다.")
    }

    fun indexOf(name: String): Int {
        return subroutineSymbols.find { it.isNameIt(name) }?.index
            ?: classSymbols.find { it.isNameIt(name) }?.index
            ?: throw RuntimeException("해당 심볼은 존재하지 않습니다.")
    }
}
