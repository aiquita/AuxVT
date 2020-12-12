package logic.terms

import logic.signature.FunctionSymbol
import logic.structures.Variable
import logic.structures.FunctionTerm
import logic.structures.Term
import logic.structures.VariableTerm

fun v(symbol: String) = VariableTerm(Variable(symbol))

fun x() = v("x")

fun y() = v("y")

fun z() = v("z")

fun w() = v("w")

fun f(vararg  terms: Term) =
    FunctionTerm(FunctionSymbol("f", terms.size), terms.toList())

fun g(vararg terms: Term) =
    FunctionTerm(FunctionSymbol("g", terms.size), terms.toList())

fun h(vararg terms: Term) =
    FunctionTerm(FunctionSymbol("h", terms.size), terms.toList())
