package geometry;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

/**
 * Created by oscar on 30/03/16.
 */
public class BersteinPolynomialDTest {
    @Test
    public void testValues(){
        BersteinPolynomial b = new BersteinPolynomial(3,2);
        BersteinPolynomialD b2 = new BersteinPolynomialD(3,2);

        for(int i = 0; i <= 100; i++)
            assertEquals(b.value(i/10.), b2.value(i/10.), 0.0001);
    }

    @Test
    public void testFirstDerivatives() {
        BersteinPolynomial b = new BersteinPolynomial(3,2);
        BersteinPolynomialD b2 = new BersteinPolynomialD(3,2);

        for(int i = 0; i <= 100; i++)
            assertEquals(b.derivative().value(i/10.), b2.derivative(1, i/10.), 0.0001);

    }
}
