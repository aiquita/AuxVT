package logic.structures

import logic.signature.FunctionSymbol

sealed class Term {

    fun vars(): Set<Variable> {
        return when(this) {
            is VariableTerm -> setOf(this.variable)
            is FunctionTerm -> this.subTerms.flatMap { it.vars() }.toSet()
        }
    }

    fun functionSymbols(): Set<FunctionSymbol> = when(this) {
        is VariableTerm -> emptySet()
        is FunctionTerm -> {
            val subFunctionSymbols = subTerms.flatMap { it.functionSymbols() }.toMutableSet()
            subFunctionSymbols.add(symbol)
            subFunctionSymbols
        }
    }

    fun substitution(toSubstitute: Variable, substitute: Term): Term {
        return when (this) {
            is VariableTerm -> when (this.variable) {
                toSubstitute -> substitute
                else -> this
            }
            is FunctionTerm -> this.copy(subTerms = this.subTerms.map { it.substitution(toSubstitute, substitute) })
        }
    }

}

data class VariableTerm(val variable : Variable) : Term()

data class FunctionTerm(val symbol: FunctionSymbol, val subTerms: List<Term>) :
    Term() {

    init {
        if (symbol.arity != subTerms.size)
            throw MalformedStructureException("Function symbol has arity " + symbol.arity + " but " + subTerms.size + " sub terms were given")
    }

}
