package io.parser.components

import io.parser.Identifier
import io.parser.ParsingException
import io.parser.Tag
import io.parser.Tokenizer
import logic.structures.*
import logic.structures.Sequent
import logic.signature.PredicateSymbol
import logic.signature.Signature
import logic.structures.Variable
import java.lang.Exception

class SequentParser(private val tokenizer: Tokenizer, private val signature: Signature) {

    private val termParser = TermParser(tokenizer, signature)

    fun sequent(): Sequent {
        val antecedent = quantifier()
        tokenizer.match(Tag.TURNSTILE)
        val succedent = quantifier()
        return Sequent(listOf(antecedent), listOf(succedent))
    }

    private fun quantifier(): Formula {
        if (tokenizer.matchIf(Tag.FORALL)) {
            val (variable, formula) = quantifierContent()
            return Quantifier(QuantorSymbol.FORALL, variable, formula)
        }
        if (tokenizer.matchIf(Tag.EXISTS)) {
            val (variable, formula) = quantifierContent()
            return Quantifier(QuantorSymbol.EXISTS, variable, formula)
        }
        return equivalence()
    }

    private fun quantifierContent(): Pair<Variable, Formula> {
        val currentToken = tokenizer.lookahead()
        tokenizer.match(Tag.ID)
        tokenizer.match(Tag.DOT)

        val varName = (currentToken as Identifier).lexeme
        val quantifiedVariable = Variable(varName)
        val formula = quantifier()

        return Pair(quantifiedVariable, formula)
    }

    private fun equivalence() = lookaheadRule(Tag.EQUIVALENCE, BinarySymbol.EQUIVALENCE, ::implication)

    private fun implication() = lookaheadRule(Tag.IMPLIES, BinarySymbol.IMPLIES, ::disjunction)

    private fun disjunction() = lookaheadRule(Tag.OR, BinarySymbol.OR, ::conjunction)

    private fun conjunction() = lookaheadRule(Tag.AND, BinarySymbol.AND, ::negation)

    private fun negation(): Formula {
        return when(tokenizer.tag()) {
            Tag.NEGATION -> {
                tokenizer.match(Tag.NEGATION)
                val formula = negation()
                Negation(formula)
            }
            else -> inseparable()
        }
    }

    /**
     * Matches formulas that are inseparable in sense of operator precedence, but which may contain sub formulas.
     */
    private fun inseparable(): Formula {
        return when (tokenizer.tag()) {
            Tag.LEFT_BRACE -> {
                tokenizer.match(Tag.LEFT_BRACE)
                val formula = equivalence()
                tokenizer.match(Tag.RIGHT_BRACE)
                formula
            }
            Tag.ID -> atomic()
            else -> throw Exception("Unexpected token " + tokenizer.tag())
        }
    }

    /**
     * Matches atomic formulas, which does not contain any sub formulas.
     */
    private fun atomic(): Formula {
        val currentToken = tokenizer.lookahead()
        val symbolName = (currentToken as Identifier).lexeme

        if (signature.hasFunctionSymbolNamed(symbolName)) {
            val leftTerm = termParser.term()
            tokenizer.match(Tag.EQUALS)
            val rightTerm = termParser.term()

            return Equality(leftTerm, rightTerm)
        }
        if (signature.hasPredicateSymbolNamed(symbolName))
            return predicate(symbolName)

        throw ParsingException(tokenizer, "Symbol " + symbolName + " is used but was not declared in the signature")
    }

    private fun predicate(symbolName: String): Predicate {
        tokenizer.match(Tag.ID)

        val arguments = termParser.termList()
        val predicateSymbol = PredicateSymbol(symbolName, arguments.size)

        if (!signature.predicateSymbols.any{ it == predicateSymbol})
            throw ParsingException(tokenizer, "Predicate symbol " + predicateSymbol + " is used but was not declared in the signature")

        return Predicate(predicateSymbol, arguments)
    }

    private fun lookaheadRule(targetOperator: Tag, binarySymbol: BinarySymbol, strongerRule: () -> Formula): Formula {
        val tiedTerm = strongerRule();
        val rest = restRule(targetOperator, binarySymbol, strongerRule)

        return if (rest == null) tiedTerm else Binary(binarySymbol, tiedTerm, rest)
    }

    private fun restRule(targetOperator: Tag, binarySymbol: BinarySymbol, strongerRule: () -> Formula): Formula? {
        if (tokenizer.wouldMatch(targetOperator)) {
            tokenizer.match(targetOperator)
            val tiedTerm = strongerRule();
            val rest = restRule(targetOperator, binarySymbol, strongerRule)

            return if (rest == null) tiedTerm else Binary(binarySymbol, tiedTerm, rest)
        }
        return null
    }

}
