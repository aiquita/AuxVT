package logic.calculus.firstorder

import logic.structures.Sequent
import logic.calculus.common.PredecessorRule
import logic.structures.*

data class Cut(val divider: Formula) : PredecessorRule() {

    override fun applicable(sequent: Sequent): Boolean = true

    override fun apply(sequent: Sequent): List<Sequent> {
        return listOf(
            sequent.withPremise(divider),
            sequent.withProposition(divider)
        )
    }

}
