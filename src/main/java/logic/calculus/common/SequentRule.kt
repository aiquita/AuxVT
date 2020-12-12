package logic.calculus.common

import logic.structures.Sequent

abstract class SequentRule() {

    abstract fun applicable(sequent: Sequent): Boolean

}
