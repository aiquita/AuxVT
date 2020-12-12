package logic.structures

import logic.terms.x
import logic.terms.y
import org.junit.Test
import kotlin.test.assertEquals

class QuantifierFormulaSubstitutionTest {

    @Test
    fun `test substitution of quantified variable`() {
        // arrange
        val formula = forall("x", eq(x(), x()))
        val toSubstitute = Variable("x")
        val substitute = y()
        // act
        val substituted = formula.substitution(toSubstitute, substitute)
        // assert
        assertEquals(formula, substituted)
    }




}
