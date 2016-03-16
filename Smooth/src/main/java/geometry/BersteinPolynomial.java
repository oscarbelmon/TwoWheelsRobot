package geometry;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.util.CombinatoricsUtils;

/**
 * Created by oscar on 15/03/16.
 */
public class BersteinPolynomial implements UnivariateFunction, DifferentiableUnivariateFunction {
    private int degree;
    private int th;
    private PolynomialFunction p;

    public BersteinPolynomial(int degree, int th) {
        super();
        this.degree = degree;
        this.th = th;
        p = new PolynomialFunction(new double[]{CombinatoricsUtils.binomialCoefficient(degree, th)});
        calculatePolynomial();
    }

    private void calculatePolynomial() {
        PolynomialFunction t = new PolynomialFunction(new double[]{0,1});
        PolynomialFunction one_t = new PolynomialFunction(new double[]{1,-1});
        for(int i = 0; i < th; i++)
            p = p.multiply(t);
        for(int i = 0; i < degree-th; i++)
            p = p.multiply(one_t);
    }

    @Override
    public String toString() {
        return p.toString();
    }

    @Override
    public double value(double t) {
        return p.value(t);
    }

    @Override
    public UnivariateFunction derivative() {
        return p.derivative();
    }

    public PolynomialFunction asPolynomialFunction() {
        return p;
    }
}
