package logic.calculus

import logic.structures.Variable
import logic.terms.f
import logic.terms.x
import logic.terms.y
import logic.terms.z
import org.junit.Test
import kotlin.test.assertEquals

class SubstitutionTest {

    @Test
    fun `test substitution of an existing variable in a variable term with another variable term`() {
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
    fun `test substitution of an existing variable in a function term with another variable term`() {
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
    fun `test substituion of an existing variable in a function term with itself`() {
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

}
