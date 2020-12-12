package printing

import logic.structures.FunctionTerm
import logic.structures.Term
import logic.structures.VariableTerm

fun FunctionTerm.getStringRepresentation(): String {
    val parameters = this.subTerms.joinToString(", ") { it.getStringRepresentation() }
    return if (this.symbol.arity == 0)
        this.symbol.name
    else
        this.symbol.name + "(" + parameters + ")"
}

fun Term.getStringRepresentation(): String {
    return when (this) {
        is VariableTerm -> this.variable.symbol
        is FunctionTerm -> this.getStringRepresentation()
    }
}
