package logic.calculus.firstorder

import logic.structures.Sequent
import logic.calculus.common.AxiomRule
import logic.structures.Equality

object EqualityReflexiv : AxiomRule() {

    override fun applicable(sequent: Sequent): Boolean = sequent.antecedent.any { it is Equality && it.leftTerm == it.rightTerm }

}
