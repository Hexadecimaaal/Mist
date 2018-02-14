package io.github.hexadecimaaal.mist

class Interpreter {
    private val myCuteRBQParser = Parser()
    private var symbolList = hashMapOf<String, MistObject>()
    fun transform(s : String) : MistObject? {
        return transform(myCuteRBQParser.parse(s))
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
