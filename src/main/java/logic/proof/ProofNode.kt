package logic.proof

import logic.calculus.common.SequentRule
import logic.structures.Sequent

abstract class ProofNode(private val innerSequent: Sequent, rule: SequentRule?) {

    val sequent = this.innerSequent

    init{
        if (rule != null && !rule.applicable(innerSequent))
            throw IllegalArgumentException()
    }

    fun hasSequent(sequent: Sequent): Boolean = sequent == this.innerSequent

    abstract fun isComplete(): Boolean

    abstract fun hasOpenGoals(): Boolean

    abstract fun getNodesWithOpenGoals(): List<ProofNode>

    abstract fun getFirstNodeWithOpenGoals(): ProofNode?

    abstract fun withProofOfLeftmost(rule: SequentRule): ProofNode
}
