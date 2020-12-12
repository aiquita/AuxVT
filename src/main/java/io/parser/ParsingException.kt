package io.parser

data class ParsingException(val line: Int, val msg: String) : Exception() {

    constructor(tokenizer: Tokenizer, message: String) : this(tokenizer.getLine(), message)

    override val message = "Line " + line + ": " + msg

}
