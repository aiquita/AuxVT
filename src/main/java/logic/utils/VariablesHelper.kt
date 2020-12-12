package logic.utils

import logic.structures.Variable

object VariablesHelper {

    fun getFreshVariable(currentVariables: Set<Variable>): Variable {
        var counter = 0
        var candidate: Variable
        do {
            val varName = "x" + counter++
            candidate = Variable(varName)
        } while (currentVariables.contains(candidate))
        return candidate
    }

}
