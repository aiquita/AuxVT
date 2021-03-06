package logic.calculus.propositional

import logic.structures.Sequent
import logic.calculus.common.PositionDependendRule
import logic.calculus.RuleUtils
import logic.structures.Binary
import logic.structures.BinarySymbol

data class AndRight(override val pos: Int) : PositionDependendRule(pos) {

    override fun applicable(sequent: Sequent): Boolean = RuleUtils.applicableInSuccedent<Binary, BinarySymbol>(sequent, pos, BinarySymbol.AND)

    override fun apply(sequent: Sequent): List<Sequent> {
        val (sequent, pivot) = RuleUtils.extractPivot<Binary>(sequent, pos)
        return listOf(
            sequent.withProposition(pivot.leftFormula),
            sequent.withProposition(pivot.rightFormula)
        )
    }

}
