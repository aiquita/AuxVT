package logic.calculus.firstorder

import logic.calculus.RuleUtils
import logic.structures.Quantifier
import logic.utils.VariablesHelper
import logic.structures.Sequent
import logic.structures.Formula

object QuantifierRuleUtils {

    fun getRefreshedPivot(sequent: Sequent, pos: Int): Formula {
        val (sequent, pivot) = RuleUtils.extractPivot<Quantifier>(sequent, pos)
        val notFresh = sequent.freeVars()
            .union(pivot.formula.freeVars().minus(pivot.quantifiedVariable))

        val freshVariable = VariablesHelper.getFreshVariable(notFresh)
        return pivot.formula.substitution(pivot.quantifiedVariable, freshVariable)
    }

}
