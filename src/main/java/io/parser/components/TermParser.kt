package io.parser.components

import io.parser.Identifier
import io.parser.ParsingException
import io.parser.Tag
import io.parser.Tokenizer
import logic.signature.FunctionSymbol
import logic.signature.Signature
import logic.structures.FunctionTerm
import logic.structures.Term
import logic.structures.Variable
import logic.structures.VariableTerm

class TermParser(private val tokenizer: Tokenizer, private val signature: Signature) {

    fun termList(): List<Term> {
        val subTerms = ArrayList<Term>()
        if (tokenizer.matchIf(Tag.LEFT_BRACE)) {
            subTerms.add(term())
            while (tokenizer.matchIf(Tag.COMMA))
                subTerms.add(term())

            tokenizer.match(Tag.RIGHT_BRACE)
        }
        return subTerms
    }

    fun term(): Term {
        val currentToken = tokenizer.lookahead()
        tokenizer.match(Tag.ID)

        val symbolName = (currentToken as Identifier).lexeme
        val subTerms = termList()
        val functionSymbol = FunctionSymbol(symbolName, subTerms.size)

        if (signature.functionSymbols.any { it == functionSymbol })
            return FunctionTerm(functionSymbol, subTerms)

        if (subTerms.isEmpty())
            return VariableTerm(Variable(symbolName))

        throw ParsingException(tokenizer, "Function symbol " + functionSymbol + " is used but was not declared in the signature")
    }



}
