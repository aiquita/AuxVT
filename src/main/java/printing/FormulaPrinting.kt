package printing

import logic.structures.*

fun Binary.getStringRepresentation(): String {
    val operands = flattenToOperands()
    return "(" + operands.joinToString(this.operator.getStringRepresentation()) { it.getStringRepresentation() } + ")"
}

fun Predicate.getStringRepresentation(): String {
    return if (this.symbol.arity == 0) {
        this.symbol.name
    } else {
        val parameters = this.arguments.joinToString(", ") { it.getStringRepresentation() }
        "($parameters)"
    }
}

fun Formula.getStringRepresentation(): String {
    return when (this) {
        is Negation -> "(!" + this.negatedFormula + ")"
        is Binary -> this.getStringRepresentation()
        is Quantifier -> "(" + this.quantor.getStringRepresentation() + this.quantifiedVariable.symbol + ". " + this.formula.getStringRepresentation() + ")"
        is Bool -> this.value.getStringRepresentation()
        is Equality -> this.leftTerm.getStringRepresentation() + " = " + this.rightTerm.getStringRepresentation()
        is Predicate -> this.getStringRepresentation()
    }
}
