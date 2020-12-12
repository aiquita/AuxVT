package logic.proof

import logic.calculus.common.AxiomRule
import logic.calculus.common.PredecessorRule
import logic.calculus.common.SequentRule
import logic.structures.Sequent

data class OpenProofNode(private val innerSequent: Sequent): ProofNode(innerSequent, null) {

    override fun isComplete() = false

    override fun hasOpenGoals() = true

    override fun getNodesWithOpenGoals() = listOf(this)

    override fun getFirstNodeWithOpenGoals() = this

    override fun withProofOfLeftmost(rule: SequentRule): ProofNode {
        if (!rule.applicable(innerSequent))
            throw IllegalArgumentException()

        if (rule is AxiomRule)
            return LeafProofNode(innerSequent, rule)

        val predecessorRule = rule as PredecessorRule
        val predecessors = predecessorRule.apply(innerSequent)
        val lemmata = predecessors.map { OpenProofNode(it) }
        return InnerProofNode(innerSequent, predecessorRule, lemmata)
    }

}
