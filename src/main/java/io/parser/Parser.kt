package io.parser

import io.parser.components.TermParser
import io.parser.components.TheoryParser
import logic.signature.Signature
import logic.structures.Term
import logic.theory.Theory
import java.io.FileReader
import java.io.File
import java.io.Reader
import java.io.StringReader

object Parser {

    private fun addTheoryKeywords(keywords: MutableMap<String, Token>) {
        keywords["theory"] = Symbol(Tag.THEORY)
        keywords["functions"] = Symbol(Tag.FUNCTION_SYMBOLS)
        keywords["predicates"] = Symbol(Tag.PREDICATE_SYMBOLS)
        keywords["variables"] = Symbol(Tag.VARIABLES)
        keywords["axioms"] = Symbol(Tag.AXIOMS)
        keywords["theorems"] = Symbol(Tag.THEOREMS)
    }

    private fun addSequentOperators(operators: MutableMap<String, Token>) {
        operators["|-"] = Operator(Tag.TURNSTILE)

        operators["!"] = Operator(Tag.NEGATION);
        operators["/\\"] = Operator(Tag.AND)
        operators["\\/"] = Operator(Tag.OR)
        operators["->"] = Operator(Tag.IMPLIES)
        operators["<->"] = Operator(Tag.EQUIVALENCE)

        operators["="] = Operator(Tag.EQUALS)
    }

    private fun addSequentKeywords(keywords: MutableMap<String, Token>) {
        keywords["exists"] = Operator(Tag.EXISTS)
        keywords["forall"] = Operator(Tag.FORALL)
    }

    private fun getTokenizer(reader: Reader): Tokenizer {
        val keywords = HashMap<String, Token>()
        val operators = HashMap<String, Token>()

        addTheoryKeywords(keywords)
        addSequentKeywords(keywords)
        addSequentOperators(operators)

        val scanner = Scanner(reader, keywords, operators)
        return Tokenizer(scanner)
    }

    fun parseTheory(file: File): Theory {
        val reader = FileReader(file)
        val tokenizer = getTokenizer(reader)
        val parser = TheoryParser(tokenizer)
        return parser.parse()
    }

    fun parseTerm(sequent: String, signature: Signature): Term {
        val reader = StringReader(sequent)
        val tokenizer = getTokenizer(reader)
        val parser = TermParser(tokenizer, signature)
        return parser.term()
    }

}
