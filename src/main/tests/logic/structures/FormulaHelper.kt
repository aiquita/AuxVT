package logic.structures

import logic.signature.PredicateSymbol

fun and(leftFormula: Formula, rightFormula: Formula): Formula = Binary(BinarySymbol.AND, leftFormula, rightFormula)

fun or(leftFormula: Formula, rightFormula: Formula): Formula = Binary(BinarySymbol.OR, leftFormula, rightFormula)

fun impl(leftFormula: Formula, rightFormula: Formula): Formula = Binary(BinarySymbol.IMPLIES, leftFormula, rightFormula)

fun iff(leftFormula: Formula, rightFormula: Formula): Formula = Binary(BinarySymbol.EQUIVALENCE, leftFormula, rightFormula)

fun eq(leftTerm: Term, rightTerm: Term): Formula = Equality(leftTerm, rightTerm)

fun b(symbol: String, vararg terms: Term): Formula = Predicate(PredicateSymbol(symbol, terms.size), terms.toList())

fun p(): Formula = b("p")

fun q(): Formula = b("q")

fun forall(quantified: String, formula: Formula) = Quantifier(QuantorSymbol.FORALL,
    Variable(quantified), formula)

fun exists(quantified: String, formula: Formula) = Quantifier(QuantorSymbol.EXISTS,
    Variable(quantified), formula)
