package io.parser

fun Char.contains(chars: Set<Char>) = this in chars

fun Char.isBrace() = this in setOf('(', ')')

fun Char.isNewline() = this == '\n' || this == '\r'
