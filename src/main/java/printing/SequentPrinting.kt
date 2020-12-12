package printing

import logic.structures.Sequent

fun Sequent.getStringRepresentation(): String {
    val antecedent = this.antecedent.joinToString(", ") { it.getStringRepresentation() }
    val succedent = this.succedent.joinToString(", ") { it.getStringRepresentation() }

    return antecedent + " |- " + succedent
}

