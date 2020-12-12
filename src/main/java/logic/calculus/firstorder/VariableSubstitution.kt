package logic.calculus.firstorder

import logic.calculus.common.PositionDependendRule
import logic.structures.Equality
import logic.structures.Sequent
import logic.structures.VariableTerm

data class VariableSubstitution(override val pos: Int) : PositionDependendRule(pos) {

    override fun applicable(sequent: Sequent): Boolean = sequent.antecedent.any { it is Equality && (it.leftTerm is VariableTerm || it.rightTerm is VariableTerm) }

    override fun apply(sequent: Sequent): List<Sequent> {
        val equality = sequent[pos] as Equality

        // Note that it is important, that the ordering is two times the same, because leftTerm and rightTerm both could be a variable
        val varTerm = if (equality.leftTerm is VariableTerm) equality.leftTerm else equality.rightTerm as VariableTerm
        val substitute = if (equality.leftTerm is VariableTerm) equality.rightTerm else equality.leftTerm

        return listOf(sequent.without(pos).substitution(varTerm.variable, substitute).withPremise(equality))
    }

}
