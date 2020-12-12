package logic.signature

data class FunctionSymbol(val name: String, val arity: Int) {

    override fun toString(): String = name + ": " + arity

}
