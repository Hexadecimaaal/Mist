package io.github.hexadecimaaal.mist.test

import org.junit.jupiter.api.Test
import io.github.hexadecimaaal.mist.*
import org.junit.jupiter.api.Assertions.assertEquals

class TestParser {
    @Test fun testIdentifier() {
        val p = Parser()
        assertEquals(p.parse("QAQ"),
                listOf(Identifier("QAQ")))
    }
//    @Test fun testList() {
//        val p = Parser()
//        assertEquals(p.parse("QAQ qwq QuQ"),
//                listOf(Identifier("QAQ"), Identifier("qwq"), Identifier("QuQ")))
//    }
    @Test fun testSExp() {
        val p = Parser()
        assertEquals(p.parse("(S (S (S O)))"),
                listOf(SExp(Identifier("S"),
                        listOf(SExp(Identifier("S"),
                                listOf(SExp(Identifier("S"),
                                        listOf(Identifier("O")))))))))
    }
//    @Test fun testSExpTwice() {
//        val p = Parser()
//        assertEquals(p.parse("(S (S (S O))) (S (S O))"),
//                listOf(SExp(Identifier("S"),
//                        listOf(SExp(Identifier("S"),
//                                listOf(SExp(Identifier("S"),
//                                        listOf(Identifier("O"))))))),
//                        SExp(Identifier("S"),
//                                listOf(SExp(Identifier("S"),
//                                        listOf(Identifier("O")))))))
//    }
    @Test fun testVect() {
        val p = Parser()
        p.parse("""
            (is [qwq]
                (->
                    (forall [P] (-> (not (not P)) P))
                    (forall [Q] (or (not Q) Q))))
                    """.trimIndent())
    }
    @Test fun testBinding() {
        val p = Parser()
        p.parse("""
            (is [++]
                (forall [X : Type]
                    (-> (list X) (list X) (list X))))
            """.trimIndent())
    }

    @Test fun testBinding2() {
        val p = Parser()
        p.parse("""

            (def [++ l1 l2]
                (case l1
                    [nil l2]
                    [(cons h t) (++ t (cons h l2))]))
            """.trimIndent())
    }
}