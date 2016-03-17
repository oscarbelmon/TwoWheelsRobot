package geometry;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.function.Sqrt;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;
/**
 * Created by oscar on 16/03/16.
 */
public class BezierCurveTest {
    private static BezierCurve bc;

    @BeforeClass
    public static void init() {
        List<Vector2D> poiints = new ArrayList<>();
        poiints.add(new Vector2D(120, 160));
        poiints.add(new Vector2D(35, 200));
        poiints.add(new Vector2D(220, 260));
        poiints.add(new Vector2D(220, 40));
        bc = new BezierCurve(poiints);
    }
    @Test
    public void lengthTest() {
        assertThat(bc.getLength(), closeTo(272.87, 0.01));
    }

    @Test
    public void test() {
        LaguerreSolver solver = new LaguerreSolver();
        PolynomialFunction p = new PolynomialFunction(new double[]{-0.25, 0, 1});
        System.out.println(solver.solve(1000, p, -1, 1));
    }

    @Test
    public void test2() {
//        PolynomialFunction p = bc.;
//        System.out.println(p.negate());
    }

    @Test
    public void test3() {
        PolynomialFunction p = new PolynomialFunction(new double[]{1,-1,1});
        Sqrt sqrt = new Sqrt();
        System.out.println(sqrt.derivative());
    }

    @Test
    public void test4() {
        RombergIntegrator integrator = new RombergIntegrator();
        System.out.println(integrator.integrate(1000000, new Funcion(), 0, 1));
//        System.out.println(integrator.integrate(1000000, new Derivada(), 0, 1));
    }

    @Test
    public void test5() {
        int params = 1;
        int order = 3;
        double xRealValue = Math.PI/4;
        DerivativeStructure x = new DerivativeStructure(params, order, 0, xRealValue);
        DerivativeStructure y = x.multiply(x).subtract(5);
//        DerivativeStructure y = x.sin();//.multiply(x).subtract(5);
        System.out.println("y    = " + y.getValue());
        System.out.println("y'   = " + y.getPartialDerivative(1));
        System.out.println("y''  = " + y.getPartialDerivative(2));
        System.out.println("y''' = " + y.getPartialDerivative(3));
    }

    private class Funcion implements DifferentiableUnivariateFunction {
        @Override
        public double value(double v) {
            return Math.sqrt(v);
        }

        @Override
        public UnivariateFunction derivative() {
            return new Derivada();
        }
    }

    private class Derivada implements UnivariateFunction {

        @Override
        public double value(double v) {
            return 1/Math.sqrt(v);
        }
    }
}
