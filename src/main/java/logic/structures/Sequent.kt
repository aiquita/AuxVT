package logic.structures

import logic.signature.FunctionSymbol
import logic.signature.PredicateSymbol
import logic.utils.with
import logic.utils.without

data class Sequent(val antecedent: List<Formula>, val succedent: List<Formula>) {

    operator fun get(pos: Int) = if (inAntecedent(pos)) antecedent[pos] else succedent[pos - antecedent.size]

    val size = antecedent.size + succedent.size

    fun inAntecedent(pos: Int) = 0 <= pos && pos < antecedent.size

    fun inSuccedent(pos: Int) = antecedent.size <= pos && pos < size

    fun withPremise(formula: Formula) =
        Sequent(this.antecedent.with(formula), this.succedent)

    fun withProposition(formula: Formula) =
        Sequent(this.antecedent, this.succedent.with(formula))

    fun without(pos: Int) = if (inAntecedent(pos))
            Sequent(this.antecedent.without(pos), this.succedent)
        else
            Sequent(this.antecedent, this.succedent.without(pos - this.antecedent.size))

    fun freeVars(): Set<Variable> = antecedent.flatMap { it.freeVars() }.union(succedent.flatMap { it.freeVars() })

    fun functionsSymbols(): Set<FunctionSymbol> = antecedent.flatMap { it.functionsSymbols() }.union(succedent.flatMap { it.functionsSymbols() })

    fun predicateSymbols(): Set<PredicateSymbol> = antecedent.flatMap { it.predicateSymbols() }.union(succedent.flatMap { it.predicateSymbols() })

    fun substitution(toSubstitute: Variable, substitute: Term): Sequent =
        Sequent(
            antecedent.map { it.substitution(toSubstitute, substitute) },
            succedent.map { it.substitution(toSubstitute, substitute) }
        )

}
