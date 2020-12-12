package io.parser.components

import io.parser.*
import logic.structures.Sequent
import logic.signature.FunctionSymbol
import logic.signature.PredicateSymbol
import logic.signature.Signature
import logic.theory.Theory
import logic.proof.ProofNode
import logic.structures.Variable

class TheoryParser(private val tokenizer: Tokenizer) {

    private var sequentParser: SequentParser? = null

    fun parse() = theory()

    private fun theory(): Theory {
        tokenizer.match(Tag.THEORY)

        val functionSymbols = functionSymbols()
        val predicateSymbols = predicateSymbols()
        val signature = Signature(functionSymbols, predicateSymbols)

        sequentParser = SequentParser(tokenizer, signature)

        val axioms = axioms()
        val theorems = theorems()
        val proofs = emptyMap<String, ProofNode>().toMutableMap()

        return Theory(signature, axioms, theorems, proofs)
    }

    private fun functionSymbols() = symbols(Tag.FUNCTION_SYMBOLS, "Function symbol ", ::functionSymbol)

    private fun predicateSymbols() = symbols(Tag.PREDICATE_SYMBOLS, "Predicate symbol ", ::predicateSymbol)

    private fun variables(signature: Signature): Set<Variable> {
        val variables = HashSet<Variable>()
        if (tokenizer.matchIf(Tag.VARIABLES)) {
            while (tokenizer.wouldMatch(Tag.ID)) {
                val currentToken = tokenizer.lookahead()
                tokenizer.match(Tag.ID)
                tokenizer.match(Tag.SEMICOLON)

                val symbol = (currentToken as Identifier).lexeme
                if(variables.any { it.symbol == symbol })
                    throw ParsingException(tokenizer, "Variable " + symbol + " was already declared")

                if (signature.symbols().contains(symbol))
                    throw ParsingException(tokenizer, "Variable " + symbol + " was already used for a function or predicate symbol")

                variables.add(Variable(symbol))
            }
        }
        return variables
    }

    private fun <T> symbols(tag: Tag, exceptionPrefix: String, symbolParser: () -> T): Set<T> {
        val symbols = HashSet<T>()
        if (tokenizer.matchIf(tag)) {
            while (tokenizer.wouldMatch(Tag.ID)) {
                val symbol = symbolParser()
                if (symbols.contains(symbol))
                    throw ParsingException(tokenizer, exceptionPrefix + " " + symbol + " was already declared")

                symbols.add(symbol)
            }
        }
        return symbols
    }

    private fun matchSymbolAndArity(): Pair<String, Int> {
        val symbolToken = tokenizer.lookahead()
        tokenizer.match(Tag.ID)
        tokenizer.match(Tag.COLON)
        val arityToken = tokenizer.lookahead()
        tokenizer.match(Tag.NUMBER)
        tokenizer.match(Tag.SEMICOLON)

        val symbol = (symbolToken as Identifier).lexeme
        val arity = (arityToken as Num).value

        return Pair(symbol, arity)
    }

    private fun functionSymbol(): FunctionSymbol {
        val (symbol, arity) = matchSymbolAndArity()
        return FunctionSymbol(symbol, arity)
    }

    private fun predicateSymbol(): PredicateSymbol {
        val (symbol, arity) = matchSymbolAndArity()
        return PredicateSymbol(symbol, arity)
    }

    private fun matchNamedSequents(listName: String): Map<String, Sequent> {
        val namedSequents = HashMap<String, Sequent>()
        while (tokenizer.wouldMatch(Tag.ID)) {
            val nameToken = tokenizer.lookahead()
            tokenizer.match(Tag.ID)
            tokenizer.match(Tag.COLON)

            val nameIdentifier = nameToken as Identifier
            val name = nameIdentifier.lexeme
            if (namedSequents.containsKey(name))
                throw ParsingException(tokenizer, "There must not be two " + listName + " with name " + name)

            namedSequents[name] = sequentParser!!.sequent()

            tokenizer.match(Tag.SEMICOLON)
        }
        return namedSequents
    }

    private fun axioms(): Map<String, Sequent> =
        if (tokenizer.matchIf(Tag.AXIOMS))
            matchNamedSequents("axioms")
        else emptyMap()

    private fun theorems(): Map<String, Sequent> =
        if (tokenizer.matchIf(Tag.THEOREMS))
            matchNamedSequents("theorems")
        else emptyMap()

}
