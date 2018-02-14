package io.github.hexadecimaaal.mist

sealed class Token
private object LParen : Token()
private object RParen : Token()
private object LBracket : Token()
private object RBracket : Token()
private object End : Token()
private object As : Token()
private data class TOperator(val name : String) : Token()
private data class TIdentifier(val name : String) : Token()

sealed class AST
data class Identifier(val name : String) : AST()
data class SExp(val head : AST, val tail : List<AST>) : AST()
data class Vect(val content : List<AST>) : AST()
data class Bind(val name : AST, val type : AST) : AST()

class Parser {
    private var pos : Int = 0
    private var input : String = ""
    public fun parse(input : String) : AST {
        this.input = input + '\uFFFF'
//        val list = ArrayList<AST>()
//        while (peekToken() != End) {
//            list.add(parseAST())
//        }
//        return list
        return parseAST()
    }

    private fun parseAST() : AST {
        val x = getToken()
        return when (x) {
            LParen -> parseSExp()
            is TIdentifier -> Identifier(x.name)
            RParen -> TODO()
            End -> TODO()
            As -> TODO()
            is TOperator -> Identifier(x.name)
            LBracket -> parseVect()
            RBracket -> TODO()
        }
    }

    private fun parseSExp() : SExp {
        val head = parseAST()
        var x = peekToken()
        val l = ArrayList<AST>()
        while (x != RParen) {
            l.add(parseAST())
            x = peekToken()
        }
        getToken() // => RParen
        return SExp(head, l)
    }

    private fun parseVect() : Vect {
        var x = peekToken()
        val l = ArrayList<AST>()
        while (x != RBracket) {
            val name = parseAST()
            x = peekToken()
            if (x == As) {
                getToken() // => As
                l.add(Bind(name, parseAST()))
                x = peekToken()
            }
            else {
                l.add(name)
            }
        }
        getToken() // => RBracket
        return Vect(l)
    }

    private tailrec fun getToken() : Token = when (input[pos]) {
        '(' -> {
            pos++
            LParen
        }
        ')' -> {
            pos++
            RParen
        }
        '[' -> {
            pos++
            LBracket
        }
        ']' -> {
            pos++
            RBracket
        }
        '\uFFFF' -> End
        in AZaz -> eatTIdentifier()
        in Space -> {
            pos++
            getToken()
        }
        ':' -> {
            pos++
            when (input[pos]) {
                in Symbols -> TOperator(":" + eatTOperator().name)
                else -> As
            }
        }
        in Symbols -> eatTOperator()
        else -> TODO()
    }

    private fun peekToken() : Token {
        val oldPos = pos
        val tok = (getToken())
        pos = oldPos
        return tok
    }

    private fun eatTIdentifier() : TIdentifier {
        var s = ""
        while (input[pos] in AZaz09) {
            s += input[pos]
            pos++
        }
        return TIdentifier(s)
    }

    private fun eatTOperator() : TOperator {
        var s = ""
        while (input[pos] in Symbols) {
            s += input[pos]
            pos++
        }
        return TOperator(s)
    }

    companion object {
        const val AZaz = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"
        const val AZaz09 = AZaz + "1234567890-"
        const val Space = " \n\t\r"
        const val Symbols = "!#$%&@`;+*:,<.>/?_=^~\\¥|-"
    }
}
