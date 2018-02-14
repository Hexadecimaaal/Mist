package io.github.hexadecimaaal.mist

import java.util.*

class Interpreter {
    private val myCuteRBQParser = Parser()
    private var symbolList = HashMap<String, MistObject>()
    fun transform(s : String) : MistObject? {
        val ast = myCuteRBQParser.parse(s)
        return transform(ast)
    }
    fun transform(ast : AST) : MistObject? {
       return when (ast) {
           is Identifier -> symbolList[ast.name]
           is SExp -> {
               val h = transform(ast.head)
               if (h == null) TODO()
               else {

               }
           }
           is Vect -> TODO()
           is Bind -> TODO()
       }
    }
}
