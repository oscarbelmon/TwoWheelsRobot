package geometry;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

/**
 * Created by oscar on 30/03/16.
 */
public class DSTest {
    @Test
    public void test() {
//        DerivativeStructure constant = new DerivativeStructure(1,1,1);
//        System.out.println(constant.getAllDerivatives());
//        for(double d: constant.getAllDerivatives())
//            System.out.println(d);

//        DerivativeStructure square = new DerivativeStructure(1,3,0,2);
//        DerivativeStructure f = square.multiply(square);
//        for(double d: f.getAllDerivatives())
//            System.out.println(d);

        double val = 0.1;
        DerivativeStructure t = new DerivativeStructure(1,2,0,val);
        DerivativeStructure one = new DerivativeStructure(1,2,1);
        DerivativeStructure one_t = one.subtract(t);
        DerivativeStructure t_one_t = t.multiply(one_t);
        for(double d: t_one_t.getAllDerivatives())
            System.out.println(d);

    }

    @Test
    public void test2(){
        BersteinPolynomial b = new BersteinPolynomial(3,2);
        System.out.println(b.value(0.5));

        BersteinPolynomialD b2 = new BersteinPolynomialD(3,2);
        System.out.println(b.value(0.5));

        for(int i = 0; i <= 10; i++)
            assertEquals(b.value(i/10.), b2.value(i/10.), 0.0001);
    }

    @Test
    public void testDerivatives() {
        double t = 0.5;
        BersteinPolynomial b = new BersteinPolynomial(3,2);
        System.out.println(b.derivative().value(t));
        BersteinPolynomialD b2 = new BersteinPolynomialD(3,2);
        System.out.println(b2.derivative(t,1));

        for(int i = 0; i <= 10; i++)
            assertEquals(b.derivative().value(i/10.), b2.derivative(i/10., 1), 0.0001);

    }
}
