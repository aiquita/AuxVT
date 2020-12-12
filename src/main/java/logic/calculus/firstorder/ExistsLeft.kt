package logic.calculus.firstorder

import logic.structures.Sequent
import logic.calculus.RuleUtils
import logic.calculus.common.PositionDependendRule
import logic.structures.*

data class ExistsLeft(override val pos: Int) : PositionDependendRule(pos) {

    override fun applicable(sequent: Sequent): Boolean = RuleUtils.applicableInSuccedent<Quantifier, QuantorSymbol>(sequent, pos, QuantorSymbol.EXISTS)

    override fun apply(sequent: Sequent): List<Sequent> {
        val refreshedPivot = QuantifierRuleUtils.getRefreshedPivot(sequent, pos)
        return listOf(sequent.withProposition(refreshedPivot))
    }

}
