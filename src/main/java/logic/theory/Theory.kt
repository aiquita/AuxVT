package logic.theory

import logic.structures.Sequent
import logic.signature.Signature
import logic.proof.ProofNode
import logic.structures.Variable

data class Theory(
    val signature: Signature,
    val axioms: Map<String, Sequent>,
    val theorems: Map<String, Sequent>,
    val proofs: MutableMap<String, ProofNode>
) {

    init {
        val nameIntersection = axioms.keys.intersect(theorems.keys)
        if (nameIntersection.any())
            throw MalformedTheoryException("Name " + nameIntersection.first() + " must not be used for a axiom and a theorem")

        val sequents = axioms.map { it.value }.union(theorems.map { it.value })

        val functionSymbols = sequents.flatMap { it.functionsSymbols() }
        functionSymbols.forEach {functionSymbol ->
            if (!signature.functionSymbols.any { it == functionSymbol })
                throw MalformedTheoryException("Function symbol " + functionSymbol + " is used but was not declared in the signature")
        }

        val predicateSymbols = sequents.flatMap { it.predicateSymbols() }
        predicateSymbols.forEach { predicateSymbol ->
            if (!signature.predicateSymbols.any { it == predicateSymbol})
                throw MalformedTheoryException("Predicate symbol " + predicateSymbol + " is used but was not declared in the signature")
        }
    }

    operator fun get(name: String) = if (axioms.keys.contains(name))
            axioms[name]
        else
            if (theorems.keys.contains(name))
                theorems[name]
            else
                throw IndexOutOfBoundsException()

}

