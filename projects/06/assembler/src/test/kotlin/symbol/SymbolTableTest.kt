package symbol

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SymbolTableTest {
    @Test
    fun `테이블에 데이터 추가`() {
        val symbolTable = SymbolTable()
        val symbol = "test"
        val address = 0

        symbolTable.add(symbol, address)

        assertTrue(symbolTable.contains(symbol))
        assertEquals(symbolTable.getAddress(symbol), address)
    }
}
