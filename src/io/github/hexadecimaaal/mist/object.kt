package io.github.hexadecimaaal.mist

sealed class MistType(type: MistType) : MistObject(type)

class Type(val order: Int) : MistType(Type(order + 1))

object Prop : MistType(Type(0))

object Set : MistType(Type(0))

class Induction(val name: String, val constructors: List<MistObject>) : MistType(Set)

class Forall(val bind: MistType, val body: MistType) : MistType(when (body) {
    is Type -> Type(body.order + 1)
    Prop -> Prop
    Set -> when (bind) {
        is Type -> Type(bind.order + 1)
        else -> Type(0)
    }
    is Forall -> body.body
    is Induction -> Set
})

sealed class MistObject(val type: MistType)

class Param(type: MistType) : MistObject(type)

sealed class Abstraction(val binder: Param, val body: MistObject) :
        MistObject(Forall(binder.type, body.type))

class Definition(binder: Param, body: MistObject) :
        Abstraction(binder, body)

class Fixpoint(binder: Param, body: MistObject) :
        Abstraction(binder, body)

sealed class Call(val function: Abstraction) :
        MistObject(function.body.type)

class CallDef(function: Definition, val param: MistObject) :
        Call(function)

class CallFix(function: Fixpoint, val param: MistObject) :
        Call(function)
