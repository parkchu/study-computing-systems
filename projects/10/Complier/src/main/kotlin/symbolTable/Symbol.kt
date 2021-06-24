package symbolTable

class Symbol(
    private val name: String,
    val type: String,
    val kind: Kind,
    val index: Int
    ) {

    fun isNameIt(name: String): Boolean = name == this.name

    fun isKindIt(kind: Kind): Boolean = kind == this.kind
}
