package logic.calculus.propositional

import logic.structures.Sequent
import logic.calculus.common.PositionDependendRule
import logic.structures.Negation

data class NegationLeft(override val pos: Int) : PositionDependendRule(pos) {

    override fun applicable(sequent: Sequent): Boolean {
        if (!sequent.inAntecedent(pos))
            return false

        return sequent[pos] is Negation
    }

    override fun apply(sequent: Sequent): List<Sequent> {
        val formulaWithoutPivot = sequent.without(pos)
        val pivot = sequent[pos]
        return listOf(formulaWithoutPivot.withProposition(pivot))
    }

}
