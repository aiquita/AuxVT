package io.parser

class Tokenizer(private val scanner: Scanner) {

    private var lookahead = scanner.scan()

    fun match(tag: Tag) {
        if (wouldMatch(tag))
            lookahead = scanner.scan()
        else
            throw ParsingException(this, "Expected Token " + tag + " but got " + (lookahead?.tag ?: "none"))
    }

    fun wouldMatch(tag: Tag) = lookahead?.tag == tag

    fun matchIf(tag: Tag): Boolean {
        return if (wouldMatch(tag)) {
            match(tag)
            true
        }
        else false
    }

    fun lookahead() = lookahead

    fun tag() = lookahead()?.tag

    fun getLine() = scanner.getLine()

}
