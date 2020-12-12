package printing

import logic.structures.BinarySymbol
import logic.structures.BoolSymbol
import logic.structures.QuantorSymbol

fun BinarySymbol.getStringRepresentation(): String {
    return when (this) {
        BinarySymbol.AND -> " /\\ "
        BinarySymbol.OR -> " \\/ "
        BinarySymbol.IMPLIES -> " -> "
        BinarySymbol.EQUIVALENCE -> " <-> "
    }
}

fun QuantorSymbol.getStringRepresentation(): String {
    return when (this) {
        QuantorSymbol.EXISTS -> "exists "
        QuantorSymbol.FORALL -> "forall "
    }
}

fun BoolSymbol.getStringRepresentation(): String {
    return when (this) {
        BoolSymbol.TRUE -> "true"
        BoolSymbol.FALSE -> "false"
    }
}
