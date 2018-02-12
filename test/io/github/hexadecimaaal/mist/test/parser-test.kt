package io.github.hexadecimaaal.mist.test

import org.junit.jupiter.api.Test
import io.github.hexadecimaaal.mist.*
import org.junit.jupiter.api.Assertions.assertEquals

class TestParser {
    @Test fun testIdentifier() {
        val p = Parser()
        assertEquals(p.parse("QAQ\u0000"),
                listOf(Identifier("QAQ")))
    }
    @Test fun testList() {
        val p = Parser()
        assertEquals(p.parse("QAQ qwq QuQ \u0000"),
                listOf(Identifier("QAQ"), Identifier("qwq"), Identifier("QuQ")))
    }
    @Test fun testSExp() {
        val p = Parser()
        assertEquals(p.parse("(S (S (S O))) \u0000"),
                listOf(SExp(Identifier("S"),
                        listOf(SExp(Identifier("S"),
                                listOf(SExp(Identifier("S"),
                                        listOf(Identifier("O")))))))))
    }
    @Test fun testSExpTwice() {
        val p = Parser()
        assertEquals(p.parse("(S (S (S O))) (S (S O))\u0000"),
                listOf(SExp(Identifier("S"),
                        listOf(SExp(Identifier("S"),
                                listOf(SExp(Identifier("S"),
                                        listOf(Identifier("O"))))))),
                        SExp(Identifier("S"),
                                listOf(SExp(Identifier("S"),
                                        listOf(Identifier("O")))))))
    }
    @Test fun testVect() {
        val p = Parser()
        p.parse("""
            (is [qwq]
                (->
                    (forall [P] (-> (not (not P)) P))
                    (forall [Q] (or (not Q) Q))))
                    """.trimIndent() + '\u0000')
    }
    @Test fun testBinding() {
        val p = Parser()
        p.parse("""
            (is [++]
                (forall [X : Type]
                    (-> (list X) (list X) (list X))))

            (def [++ l1 l2]
                (case l1
                    [ [_ : nil] l2
                      [[h t] : cons] (++ t (cons h l2))] ))
            """.trimIndent() + '\u0000')
    }
}