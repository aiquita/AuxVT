package logic.terms

import logic.structures.Variable
import org.junit.Test
import kotlin.test.assertEquals

class TermSubstitutionTest {

    @Test
    fun `test substitution in a variable term with another variable term`() {
        // arrange
        val variableTerm = x()
        val toSubstitute = Variable("x")
        val substitute = y()
        // act
        val substitutedTerm = variableTerm.substitution(toSubstitute, substitute)
        //assert
        assertEquals(substitute, substitutedTerm)
    }

    @Test
    fun `test substitution of an not existing variable in a variable term with another variable term`() {
        // arrange
        val variableTerm = x()
        val toSubstitute = Variable("z")
        val substitute = y()
        // act
        val substitutedTerm = variableTerm.substitution(toSubstitute, substitute)
        // assert
        assertEquals(variableTerm, substitutedTerm)
    }

    @Test
    fun `test substitution in a function term with another variable term`() {
        // arrange
        val functionTerm = f(
            x(),
            f(y(), x())
        )
        val toSubstitute = Variable("x")
        val substitute = z()
        val expected = f(
            z(),
            f(y(), z())
        )
        // act
        val substitutedTerm = functionTerm.substitution(toSubstitute, substitute)
        // assert
        assertEquals(expected, substitutedTerm)
    }

    @Test
    fun `test substitution of an not existing variable in a function term with another variable term`() {
        // arrange
        val functionTerm = f(
            x(),
            f(y(), x())
        )
        val toSubstitute = Variable("z")
        val substitute = x()
        // act
        val substitutedTerm = functionTerm.substitution(toSubstitute, substitute)
        // assert
        assertEquals(functionTerm, substitutedTerm)
    }

    @Test
    fun `test substitution of an variable in a function term with itself`() {
        // arrange
        val functionTerm = f(
            x(),
            f(y(), z())
        )
        val toSubstitute = Variable("y")
        val substitute = y()
        // act
        val substitutedTerm = functionTerm.substitution(toSubstitute, substitute)
        // assert
        assertEquals(functionTerm, substitutedTerm)
    }

    @Test
    fun `test substitution with another function term`() {
        // arrange
        val functionTerm = f(
            x(),
            f(y(), z())
        )
        val toSubstitute = Variable("y")
        val substitute =
            g(w(), x())
        val expected = f(
            x(),
            f(
                g(w(), x()),
                z()
            )
        )
        //act
        val substitutedTerm = functionTerm.substitution(toSubstitute, substitute)
        // assert
        assertEquals(expected, substitutedTerm)
    }

    @Test
    fun `test simultaneously substitution at two positions`() {
        // arrange
        val functionTerm = f(x(), z(), x())
        val toSubstitute = Variable("x")
        val substitute = g(y(), w())
        val expected = f(
            g(y(), w()),
            z(),
            g(y(), w())
        )
        // act
        val substituted = functionTerm.substitution(toSubstitute, substitute)
        // assert
        assertEquals(expected, substituted)
    }

    @Test
    fun `test of two consecutive substitutions`() {
        // arrange
        val functionTerm =
            f(x(), y())
        val firstToSubstitute = Variable("y")
        val firstSubstitute =
            g(z(), w())
        val secondToSubsitute = Variable("z")
        val secondSubstitute =
            h(x(), y())
        val expected = f(
            x(),
            g(
                h(x(), y()),
                w()
            )
        )
        // act
        var substituted = functionTerm.substitution(firstToSubstitute, firstSubstitute)
        substituted = substituted.substitution(secondToSubsitute, secondSubstitute)
        // assert
        assertEquals(expected, substituted)
    }

}
