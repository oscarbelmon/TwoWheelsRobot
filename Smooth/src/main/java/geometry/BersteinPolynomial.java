package geometry;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.util.CombinatoricsUtils;

/**
 * Created by oscar on 30/03/16.
 */
public class BersteinPolynomial {
    private int degree;
    private int th;

    public BersteinPolynomial(int degree, int th) {
        super();
        this.degree = degree;
        this.th = th;
    }

    public DerivativeStructure getPolynomial(double val) {
        DerivativeStructure p = new DerivativeStructure(1, degree, CombinatoricsUtils.binomialCoefficient(degree, th));
        DerivativeStructure t = new DerivativeStructure(1, degree, 0, val);
        DerivativeStructure one = new DerivativeStructure(1, degree, 1);
        DerivativeStructure one_t = one.subtract(t);

        for(int i = 0; i < th; i++)
            p = p.multiply(t);

        for(int i = 0; i < degree-th; i++)
            p = p.multiply(one_t);

        return p;
    }

    public double value(double val) {
        return getPolynomial(val).getValue();
    }

    public double derivative(int degree, double val) {
        return getPolynomial(val).getPartialDerivative(degree);
    }

    public double firstDerivative(double val) {
        return derivative(1, val);
    }

    public double secondDerivative(double val) {
        return derivative(2, val);
    }
}
