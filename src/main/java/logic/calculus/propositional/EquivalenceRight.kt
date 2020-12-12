package logic.calculus.propositional

import logic.structures.Sequent
import logic.calculus.common.PositionDependendRule
import logic.calculus.RuleUtils
import logic.structures.Binary
import logic.structures.BinarySymbol

data class EquivalenceRight(override val pos: Int) : PositionDependendRule(pos) {

    override fun applicable(sequent: Sequent): Boolean = RuleUtils.applicableInSuccedent<Binary, BinarySymbol>(sequent, pos, BinarySymbol.EQUIVALENCE)

    override fun apply(sequent: Sequent): List<Sequent> {
        val (sequent, pivot) = RuleUtils.extractPivot<Binary>(sequent, pos)
        return listOf(
            sequent.withPremise(pivot.leftFormula)
                .withProposition(pivot.rightFormula),
            sequent.withPremise(pivot.rightFormula)
                .withProposition(pivot.leftFormula)
        )
    }

}
