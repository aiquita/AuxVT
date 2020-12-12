package logic.calculus.common

import logic.structures.Sequent

abstract class PredecessorRule() : SequentRule() {

    abstract fun apply(sequent: Sequent): List<Sequent>

}
