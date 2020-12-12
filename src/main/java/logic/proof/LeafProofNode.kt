package logic.proof

import logic.calculus.common.AxiomRule
import logic.calculus.common.SequentRule
import logic.structures.Sequent

class LeafProofNode(sequent: Sequent, rule : AxiomRule) : ProofNode(sequent, rule)  {

    override fun isComplete() = true

    override fun hasOpenGoals() = false

    override fun getNodesWithOpenGoals() = emptyList<ProofNode>()

    override fun getFirstNodeWithOpenGoals(): ProofNode? = null

    override fun withProofOfLeftmost(rule: SequentRule): ProofNode = this
}
