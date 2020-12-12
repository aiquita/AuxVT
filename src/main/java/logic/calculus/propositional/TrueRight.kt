package logic.calculus.propositional

import logic.structures.Sequent
import logic.calculus.common.AxiomRule
import logic.structures.Bool
import logic.structures.BoolSymbol

object TrueRight : AxiomRule() {

    override fun applicable(sequent: Sequent): Boolean = sequent.succedent.any { it is Bool && it.value == BoolSymbol.TRUE }

}
