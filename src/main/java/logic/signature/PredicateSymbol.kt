package logic.signature

data class PredicateSymbol(val name: String, val arity: Int) {

    override fun toString(): String = name + ": " + arity

}
