package logic.structures

import logic.signature.FunctionSymbol
import logic.signature.PredicateSymbol
import logic.utils.VariablesHelper

sealed class Formula {

    fun freeVars(): Set<Variable> = when (this) {
        is Negation -> negatedFormula.freeVars()
        is Binary -> leftFormula.freeVars().union(rightFormula.freeVars())
        is Quantifier -> formula.freeVars().minus(quantifiedVariable)
        is Bool -> emptySet()
        is Equality -> leftTerm.vars().union(rightTerm.vars())
        is Predicate -> arguments.flatMap { it.vars() }.toSet()
    }

    fun functionsSymbols(): Set<FunctionSymbol> = when(this) {
        is Negation -> negatedFormula.functionsSymbols()
        is Binary -> leftFormula.functionsSymbols().union(rightFormula.functionsSymbols())
        is Quantifier -> formula.functionsSymbols()
        is Bool -> emptySet()
        is Equality -> leftTerm.functionSymbols().union(rightTerm.functionSymbols())
        is Predicate -> arguments.flatMap { it.functionSymbols() }.toSet()
    }

    fun predicateSymbols(): Set<PredicateSymbol> = when(this) {
        is Negation -> negatedFormula.predicateSymbols()
        is Binary -> leftFormula.predicateSymbols().union(rightFormula.predicateSymbols())
        is Quantifier -> formula.predicateSymbols()
        is Bool -> emptySet()
        is Equality -> emptySet()
        is Predicate -> setOf(symbol)
    }

    open fun substitution(toSubstitute: Variable, substitute: Term): Formula {
        return when (this) {
            is Negation -> Negation(negatedFormula.substitution(toSubstitute, substitute))
            is Binary -> copy(leftFormula = leftFormula.substitution(toSubstitute, substitute), rightFormula = rightFormula.substitution(toSubstitute, substitute))
            is Quantifier -> substitution(toSubstitute, substitute)
            is Bool -> this
            is Equality -> Equality(leftTerm.substitution(toSubstitute, substitute), rightTerm.substitution(toSubstitute, substitute))
            is Predicate -> copy(arguments = arguments.map { it.substitution(toSubstitute, substitute) })
        }
    }

    fun substitution(toSubstitute: Variable, substitute: Variable): Formula = substitution(toSubstitute,
        VariableTerm(substitute)
    )

    fun flattenToOperands(operator: BinarySymbol): List<Formula> {
        val operands: ArrayList<Formula> = ArrayList()
        return if (this is Binary && this.operator == operator) {
            // implies is assumed to be right associative
            // therefore we do not go into depth recursively in case of IMPLIES
            if (operator != BinarySymbol.IMPLIES)
                operands.addAll(leftFormula.flattenToOperands(operator))
            else
                operands.add(leftFormula)

            operands.addAll(rightFormula.flattenToOperands(operator))
            operands
        } else {
            operands.add(this)
            operands
        }
    }

}

enum class BinarySymbol {
    AND, OR, IMPLIES, EQUIVALENCE
}

enum class QuantorSymbol {
    EXISTS, FORALL
}

enum class BoolSymbol {
    TRUE, FALSE
}

data class Negation(val negatedFormula: Formula) : Formula()
data class Binary(val operator: BinarySymbol, val leftFormula: Formula, val rightFormula: Formula) : Formula() {

    fun flattenToOperands(): List<Formula> = flattenToOperands(operator)

}

data class Quantifier(val quantor: QuantorSymbol, val quantifiedVariable: Variable, val formula: Formula) : Formula() {

    override fun substitution(toSubstitute: Variable, substitute: Term): Quantifier {
        return when {
            quantifiedVariable == toSubstitute
                    || !this.formula.freeVars().contains(toSubstitute) -> this
            !substitute.vars().contains(quantifiedVariable)
                    && formula.freeVars().contains(toSubstitute) -> copy(formula = formula.substitution(toSubstitute, substitute))
            else -> {
                val freeVars = this.formula.freeVars().union(substitute.vars())
                val freshVariable = VariablesHelper.getFreshVariable(freeVars)
                val intermediateSubstitution = this.formula.substitution(this.quantifiedVariable,
                    VariableTerm(freshVariable)
                )
                copy(quantifiedVariable = freshVariable, formula = intermediateSubstitution.substitution(toSubstitute, substitute))
            }
        }
    }

}
data class Bool(val value: BoolSymbol) : Formula()
data class Equality(val leftTerm: Term, val rightTerm: Term) : Formula()
data class Predicate(val symbol: PredicateSymbol, val arguments: List<Term>) : Formula() {

    init {
        if (symbol.arity != arguments.size)
            throw MalformedStructureException("Predicate symbol has arity " + symbol.arity + " but " + arguments.size + " argument terms were given")
    }

}




