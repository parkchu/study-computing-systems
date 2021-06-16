package symbolTable

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SymbolTableTest {

    @Test
    fun defineSymbol() {
        val symbolTable = SymbolTable()

        symbolTable.define("name", "String", Kind.VAR)

        assertThat(symbolTable.varCount(Kind.VAR)).isEqualTo(1)
    }

    @Test
    fun startSubroutine() {
        val symbolTable = SymbolTable()
        symbolTable.define("name", "String", Kind.VAR)

        symbolTable.startSubroutine()

        assertThat(symbolTable.varCount(Kind.VAR)).isEqualTo(0)
    }

    @Test
    fun kindOf() {
        val symbolTable = SymbolTable()
        symbolTable.define("name", "String", Kind.VAR)

        val kind = symbolTable.kindOf("name")
        val none = symbolTable.kindOf("none")

        assertThat(kind).isEqualTo(Kind.VAR)
        assertThat(none).isEqualTo(Kind.NONE)
    }

    @Test
    fun typeOf() {
        val symbolTable = SymbolTable()
        symbolTable.define("name", "String", Kind.VAR)

        val type = symbolTable.typeOf("name")

        assertThat(type).isEqualTo("String")
    }

    @Test
    fun indexOf() {
        val symbolTable = SymbolTable()
        symbolTable.define("name", "String", Kind.VAR)

        val index = symbolTable.indexOf("name")

        assertThat(index).isEqualTo(0)
    }

    @Test
    fun sameName() {
        val symbolTable = SymbolTable()
        symbolTable.define("name", "String", Kind.STATIC)
        assertThat(symbolTable.kindOf("name")).isEqualTo(Kind.STATIC)
        symbolTable.define("name", "String", Kind.VAR)

        val kind = symbolTable.kindOf("name")

        assertThat(kind).isEqualTo(Kind.VAR)
    }
}
