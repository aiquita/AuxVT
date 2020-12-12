package logic.calculus.propositional

import logic.structures.Sequent
import logic.calculus.common.AxiomRule
import logic.structures.Bool
import logic.structures.BoolSymbol

object FalseLeft : AxiomRule() {

    override fun applicable(sequent: Sequent): Boolean = sequent.antecedent.any { it is Bool && it.value == BoolSymbol.FALSE }

}
