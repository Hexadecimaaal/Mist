package io.github.hexadecimaaal.mist

sealed class Token
private object LBrace : Token()
private object RBrace : Token()
private object End : Token()
private data class TIdentifier(val name : String) : Token()

sealed class AST
data class Identifier(val name : String) : AST()
data class SExp(val head : AST, val tail : List<AST>) : AST()

class Parser {
    private var pos : Int = 0
    private var input : String = ""
    fun parse(input : String) : List<AST> {
        this.input = input
        val list = ArrayList<AST>()
        while (peekToken() !is End) {
            list.add(parseAST())
        }
        return list
    }

    private fun parseAST() : AST {
        val x = getToken()
        return when (x) {
            is LBrace -> parseList()
            is TIdentifier -> Identifier(x.name)
            else -> throw Error()
        }
    }

    private fun parseList() : SExp {
        val head = parseAST()
        var x = peekToken()
        val l = ArrayList<AST>()
        while (x !is RBrace) {
            l.add(parseAST())
            x = peekToken()
        }
        getToken() // => RBrace
        return SExp(head, l)
    }

    private tailrec fun getToken() : Token = when(input[pos]) {
        '(' -> { pos++; LBrace }
        ')' -> { pos++; RBrace }
        '\u0000' -> End
        in AZaz -> eatTIdentifier()
        in Space -> { pos++; getToken() }
        else -> throw Error("qaq")
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


    companion object {
        const val AZaz = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"
        val AZaz09 = AZaz + "1234567890"
        val Space = " \n\t\r"
    }
}
