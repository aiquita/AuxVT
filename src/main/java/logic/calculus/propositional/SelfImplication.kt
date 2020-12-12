package logic.calculus.propositional

import logic.structures.Sequent
import logic.calculus.common.AxiomRule

object SelfImplication : AxiomRule() {

    override fun applicable(sequent: Sequent): Boolean = sequent.antecedent.any { left -> sequent.succedent.any { left == it } }

}
