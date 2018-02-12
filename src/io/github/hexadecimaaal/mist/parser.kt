package io.github.hexadecimaaal.mist

sealed class Token
object LBrace : Token()
object RBrace : Token()
object End : Token()
data class Identifier(val name : String) : Token()

sealed class AST
data class Iden(val name : String) : AST()
data class SExp(val head : AST, val tail : List<AST>) : AST()

class Parser {
    var pos : Int = 0
    var input : String = ""
    public fun parse(input : String) : List<AST> {
        this.input = input
        var list = ArrayList<AST>()
        while (peekToken() !is End) {
            list.add(parseAST())
        }
        return list
    }

    fun parseAST() : AST {
        val x = getToken()
        return when (x) {
            is LBrace -> parseList()
            is Identifier -> Iden(x.name)
            else -> throw Error()
        }
    }

    fun parseList() : SExp {
        val head = parseAST()
        var x = peekToken()
        var l = ArrayList<AST>()
        while (x !is RBrace) {
            l.add(parseAST())
            x = peekToken()
        }
        getToken() // => RBrace
        return SExp(head, l)
    }

    tailrec fun getToken() : Token = when(input[pos]) {
        '(' -> { pos++; LBrace }
        ')' -> { pos++; RBrace }
        '\u0000' -> End
        in AZaz -> eatIdent()
        in Space -> { pos++; getToken() }
        else -> throw Error("qaq")
    }

    fun peekToken() : Token {
        val oldPos = pos
        val tok = (getToken())
        pos = oldPos
        return tok
    }

    fun eatIdent() : Identifier {
        var s = ""
        while (input[pos] in AZaz09) {
            s += input[pos]
            pos++
        }
        return Identifier(s)
    }


    companion object {
        val AZaz = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"
        val AZaz09 = AZaz + "1234567890"
        val Space = " \n\t\r"
    }
}
