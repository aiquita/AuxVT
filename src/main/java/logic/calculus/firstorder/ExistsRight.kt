package logic.calculus.firstorder

import logic.structures.Sequent
import logic.calculus.common.PositionDependendRule
import logic.calculus.RuleUtils
import logic.structures.*
import logic.structures.Term

data class ExistsRight(override val pos: Int, val substitute: Term) : PositionDependendRule(pos) {

    override fun applicable(sequent: Sequent): Boolean = RuleUtils.applicableInSuccedent<Quantifier, QuantorSymbol>(sequent, pos, QuantorSymbol.EXISTS)

    override fun apply(sequent: Sequent): List<Sequent> {
        val (sequent, pivot) = RuleUtils.extractPivot<Quantifier>(sequent, pos)
        val substituted = pivot.formula.substitution(pivot.quantifiedVariable, substitute)
        return listOf(sequent.withProposition(substituted)
            .withProposition(pivot))
    }

}
