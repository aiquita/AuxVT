package io.parser

enum class Tag {
    LEFT_BRACE, RIGHT_BRACE, COLON, DOT, COMMA, SEMICOLON,
    NUMBER,
    THEORY, FUNCTION_SYMBOLS, PREDICATE_SYMBOLS, VARIABLES, AXIOMS, THEOREMS,
    TURNSTILE, ID, EQUALS,
    NEGATION, AND, OR, IMPLIES, EQUIVALENCE, FORALL, EXISTS
}

sealed class Token(open val tag: Tag)
data class Operator(override  val tag: Tag) : Token(tag)
data class Symbol(override val tag: Tag): Token(tag)
data class Identifier(override val tag: Tag, val lexeme: String): Token(tag)
data class Num(override val tag: Tag, val value: Int): Token(tag)
