package logic.proof

import logic.calculus.common.PredecessorRule
import logic.calculus.common.SequentRule
import logic.structures.Sequent

class InnerProofNode(sequent: Sequent, private val rule: PredecessorRule, private val lemmata: List<ProofNode>): ProofNode(sequent, rule) {

    init {
        val predecessorSequents = rule.apply(sequent)
        if (predecessorSequents.size != lemmata.size)
            throw IllegalArgumentException()

        if (lemmata.foldIndexed (false, { key, acc, proofNode -> acc || !proofNode.hasSequent(predecessorSequents[key])}))
            throw IllegalArgumentException()
    }

    override fun isComplete() = !hasOpenGoals()

    override fun hasOpenGoals() = lemmata.any { it. hasOpenGoals() }

    override fun getNodesWithOpenGoals(): List<ProofNode> = lemmata.flatMap { proofNode -> proofNode.getNodesWithOpenGoals() }

    override fun getFirstNodeWithOpenGoals(): ProofNode? = getNodesWithOpenGoals().firstOrNull()

    override fun withProofOfLeftmost(rule: SequentRule): ProofNode {
        val lemmataLeftmostProven = ArrayList<ProofNode>()

        var threshold = false // indicates if we have already seen the leftmost open lemma
        for (lemma in this.lemmata) {
            if (threshold || lemma.isComplete()) {
                lemmataLeftmostProven.add(lemma)
            } else {
                val lemmaLeftmostProven = lemma.withProofOfLeftmost(rule)
                lemmataLeftmostProven.add(lemmaLeftmostProven)
                threshold = true
            }
        }

        return InnerProofNode(sequent, this.rule, lemmataLeftmostProven)
    }

}
