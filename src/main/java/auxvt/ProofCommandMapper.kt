package auxvt

import logic.calculus.common.AxiomRule
import logic.calculus.common.PositionDependendRule
import logic.calculus.firstorder.ExistsRight
import logic.calculus.firstorder.ForAllLeft
import logic.calculus.propositional.*
import logic.structures.Term

object ProofCommandMapper {

    fun hasAxiomRule(ruleName: String) = ruleName in listOf("fl", "tr", "si")

    fun mapToAxiomRule(ruleName: String): AxiomRule {
        return when(ruleName) {
            "fl" -> FalseLeft
            "tr" -> TrueRight
            "si" -> SelfImplication
            else -> throw IllegalArgumentException()
        }
    }

    fun hasPositionDependendRule(ruleName: String) = ruleName in listOf("al", "ar", "el", "er", "il", "ir", "nl", "nr", "ol", "or")

    fun mapToPositionDependendRule(ruleName: String, pos: Int): PositionDependendRule {
        return when(ruleName) {
            "al" -> AndLeft(pos)
            "ar" -> AndRight(pos)
            "el" -> EquivalenceLeft(pos)
            "er" -> EquivalenceRight(pos)
            "il" -> ImplicationLeft(pos)
            "ir" -> ImplicationRight(pos)
            "nl" -> NegationLeft(pos)
            "nr" -> NegationRight(pos)
            "ol" -> OrLeft(pos)
            "or" -> OrRight(pos)
            else -> throw IllegalArgumentException()
        }
    }

    fun hasSubstitutionRule(ruleName: String) = ruleName in listOf("fal", "exr")

    fun mapToSubstitutionRule(ruleName: String, pos: Int, substitute: Term): PositionDependendRule {
        return when (ruleName) {
            "fal" -> ForAllLeft(pos, substitute)
            "exr" -> ExistsRight(pos, substitute)
            else -> throw IllegalArgumentException()
        }
    }

}
