package io.github.hexadecimaaal.mist.test

import org.junit.jupiter.api.Test
import io.github.hexadecimaaal.mist.*
import org.junit.jupiter.api.Assertions.assertEquals

class TestParser {
    @Test fun test1() {
        val p = Parser()
        assertEquals(p.parse("QAQ\u0000"),
                listOf(Identifier("QAQ")))
    }
    @Test fun test2() {
        val p = Parser()
        assertEquals(p.parse("QAQ qwq QuQ \u0000"),
                listOf(Identifier("QAQ"), Identifier("qwq"), Identifier("QuQ")))
    }
    @Test fun test3() {
        val p = Parser()
        assertEquals(p.parse("(S (S (S O))) \u0000"),
                listOf(SExp(Identifier("S"),
                        listOf(SExp(Identifier("S"),
                                listOf(SExp(Identifier("S"),
                                        listOf(Identifier("O")))))))))
    }
}