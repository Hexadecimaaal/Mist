package io.github.hexadecimaaal.mist

sealed class MistType(type : MistType) : MistObject(type)

data class Type(val order : Int) : MistType(Type(order + 1))

object Prop : MistType(Type(0))

object Set : MistType(Type(0))

data class Induction(val name : String, val constructors : List<MistObject>) : MistType(Set)

data class Forall(val bind : MistType, val body : MistType) : MistType(when (body) {
    is Type -> Type(body.order + 1)
    Prop -> Prop
    Set -> when (bind) {
        is Type -> Type(bind.order + 1)
        else -> Type(0)
    }
    is Forall -> body.body
    is Induction -> Set
})

sealed class MistObject(val type : MistType)

class Param(type : MistType) : MistObject(type)

sealed class Abstraction(binder : Param, body : MistObject, val bodyBody: MistObject) :
        MistObject(Forall(binder.type, body.type))

data class Definition(val binder : Param, val body : MistObject) :
        Abstraction(binder, body, body)

data class Fixpoint(val binder : Param, val body : MistObject) :
        Abstraction(binder, body, body)

sealed class Call(function: Abstraction) :
        MistObject(function.bodyBody.type)

data class CallDef(val function : Definition, val param : MistObject) :
        Call(function)

data class CallFix(val function : Fixpoint, val param : MistObject) :
        Call(function)
