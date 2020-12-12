package logic.signature

data class Signature(val functionSymbols: Set<FunctionSymbol>, val predicateSymbols: Set<PredicateSymbol>) {

    init {
        val intersection = functionSymbols.map { it.name }.intersect(predicateSymbols.map { it.name })
        if (intersection.any())
            throw MalformedSignatureException("Identifier " + intersection.first() + " must not be used for functions symbol and predicate symbols")
    }

    fun symbols() = functionSymbols.map { it.name }.union(predicateSymbols.map { it.name })

    fun hasFunctionSymbolNamed(symbolName: String) = functionSymbols.any { it.name ==  symbolName }

    fun hasPredicateSymbolNamed(symbol: String) = predicateSymbols.any { it.name ==  symbol }

}
