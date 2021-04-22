package code

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class CodeTest {
    @Test
    fun `comp 의 a 값이 상황에 맞게 변환되는지`() {
        val comp = "D+A"
        val comp2 = "D+M"

        val result = Code.changeCompToBit(comp)
        val result2 = Code.changeCompToBit(comp2)

        assertTrue(result.first().toString() == "0")
        assertTrue(result2.first().toString() == "1")
    }
}
