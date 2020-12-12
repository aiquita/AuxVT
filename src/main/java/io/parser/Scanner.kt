package io.parser

import java.io.Reader

class Scanner(private val reader: Reader, private val keywords: Map<String, Token>, private val operators: Map<String, Token>) {

    private var peek: Int = -1

    private var curChar: Char = '\u0000'

    private var line: Int = 1

    private val stringTable = keywords.toMutableMap()

    private val operatorsTable = operators.toMutableMap()

    private val operatorChars = operatorsTable.flatMap { it.key.toCharArray().toList() }.toMutableSet()

    private fun next() {
        peek = reader.read()
        curChar = peek.toChar()
    }

    init {
        stringTable["("] = Symbol(Tag.LEFT_BRACE)
        stringTable[")"] = Symbol(Tag.RIGHT_BRACE)
        operatorsTable[":"] = Symbol(Tag.COLON)
        operatorsTable["."] = Symbol(Tag.DOT)
        operatorsTable[","] = Symbol(Tag.COMMA)
        operatorsTable[";"] = Symbol(Tag.SEMICOLON)
        // they are not added above because init is executed after property initialization
        operatorChars.add(':')
        operatorChars.add('.')
        operatorChars.add(',')
        operatorChars.add(';')
        next()
    }

    private fun skipWhitespace() {
        while (curChar.isWhitespace()) {
            if (curChar.isNewline())
                line++

            next()
        }
    }

    private fun readWord(): String {
        val lexeme = StringBuilder()
        do {
            lexeme.append(curChar)
            next()
        } while (curChar.isLetter())
        return lexeme.toString()
    }

    private fun readOperator(): String {
        val lexeme = StringBuilder()
        do {
            lexeme.append(curChar)
            next()
        } while(operatorChars.contains(curChar))
        return lexeme.toString()
    }

    fun scan(): Token? {
        skipWhitespace()

        if (peek == -1)
            return null

        if (curChar.isDigit()) {
            var value = 0
            do {
                value = 10 * value + curChar.toString().toInt()
                next()
            } while (curChar.isDigit())
            return Num(Tag.NUMBER, value)
        }

        if (curChar.isLetter()) {
            val lexeme = readWord()
            return if (stringTable.containsKey(lexeme)) {
                stringTable[lexeme]!!
            } else {
                val identifier =
                    Identifier(Tag.ID, lexeme)
                stringTable[lexeme] = identifier
                identifier
            }
        }

        if (curChar.isBrace()) {
            val formerChar = curChar
            next()
            return stringTable[formerChar.toString()]!!
        }

        if (curChar.contains(operatorChars)) {
            val lexeme = readOperator()
            if (operatorsTable.containsKey(lexeme))
                return operatorsTable[lexeme]!!
        }

        var lexeme = ""
        while (!curChar.isWhitespace()) {
            lexeme += curChar
            next()
        }

        throw ParsingException(line, "Unknown lexical unit " + lexeme)
    }

    fun getLine() = line

}
