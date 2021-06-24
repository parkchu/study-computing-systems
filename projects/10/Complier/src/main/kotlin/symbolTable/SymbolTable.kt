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
    var isMethod: Boolean = false
        private set
    var className: String = ""
        private set
    var subroutineName: String = ""
        private set
    var subroutineReturnType: String = ""
        private set
    var subroutineType: String = ""
        private set

    fun init() {
        classSymbols.removeIf { it.kind == Kind.FIELD }
        className = ""
        startSubroutine()
    }

    fun setClassName(name: String) {
        className = name
    }

    fun setSubroutineName(name: String) {
        subroutineName = name
    }

    fun setSubroutineReturnType(type: String) {
        subroutineReturnType = type
    }

    fun setSubroutineType(type: String) {
        subroutineType = type
    }

    fun startSubroutine() {
        subroutineSymbols.clear()
        subroutineName = ""
        subroutineReturnType = ""
        subroutineType = ""
        isMethod = false
    }

    fun setMethodTrue() {
        isMethod = true
    }

    fun define(name: String, type: String, kind: Kind) {
        val index = if (kind == Kind.ARG && isMethod) {
            symbols.findLast { it.isKindIt(kind) }?.index ?: 0
        } else {
            symbols.findLast { it.isKindIt(kind) }?.index ?: -1
        }
        val symbols = map[kind] ?: throw RuntimeException("$kind")
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
            ?: ""
    }

    fun indexOf(name: String): Int {
        return subroutineSymbols.find { it.isNameIt(name) }?.index
            ?: classSymbols.find { it.isNameIt(name) }?.index
            ?: -1
    }
}
