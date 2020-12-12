package logic.calculus

import logic.structures.*
import logic.structures.Sequent

object RuleUtils {

    inline fun <reified T: Formula, E: Enum<E>> applicableInAntecedent(sequent: Sequent, pos: Int, enum: Enum<E>): Boolean {
        if (!sequent.inAntecedent(pos))
            return false

        return applicableAtPos<T, E>(
            sequent,
            pos,
            enum
        )
    }

    inline fun <reified T: Formula, E: Enum<E>> applicableInSuccedent(sequent: Sequent, pos: Int, enum: Enum<E>): Boolean {
        if (!sequent.inSuccedent(pos))
            return false

        return applicableAtPos<T, E>(
            sequent,
            pos,
            enum
        )
    }

    inline fun <reified T: Formula, E: Enum<E>> applicableAtPos(sequent: Sequent, pos: Int, enum: Enum<E>): Boolean {

        val formula = sequent[pos]
        if (formula !is T)
            return false

        return when(formula) {
            is Binary -> {
                val binary = formula as Binary
                binary.operator == enum
            }
            is Quantifier -> {
                val quantifier = formula as Quantifier
                quantifier.quantor == enum
            }
            is Bool -> {
                val bool = formula as Bool
                bool.value == enum
            }
            else -> true
        }
    }

    fun <T> extractPivot(sequent: Sequent, pos: Int): Pair<Sequent, T> {
        val formula = sequent[pos]
        val sequentWithoutPivot = sequent.without(pos)
        return Pair(sequentWithoutPivot, formula as T)
    }

}
