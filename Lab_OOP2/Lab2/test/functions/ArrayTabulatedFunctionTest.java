package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionTest {

    @Test
    public void testConstructorWithArrays() {
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};

        ArrayTabulatedFunction func = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(3, func.getCount());
        assertEquals(0.0, func.leftBound(), 1e-10);
        assertEquals(2.0, func.rightBound(), 1e-10);
    }

    @Test
    public void testConstructorWithFunction() {
        MathFunction sqr = new SqrFunction();
        ArrayTabulatedFunction func = new ArrayTabulatedFunction(sqr, 0.0, 2.0, 3);

        assertEquals(3, func.getCount());
        assertEquals(0.0, func.getX(0), 1e-10);
        assertEquals(1.0, func.getX(1), 1e-10);
        assertEquals(2.0, func.getX(2), 1e-10);

        assertEquals(0.0, func.getY(0), 1e-10);
        assertEquals(1.0, func.getY(1), 1e-10);
        assertEquals(4.0, func.getY(2), 1e-10);
    }

    @Test
    public void testGetSetY() {
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};
        ArrayTabulatedFunction func = new ArrayTabulatedFunction(xValues, yValues);

        func.setY(1, 5.0);
        assertEquals(5.0, func.getY(1), 1e-10);
    }

    @Test
    public void testIndexOf() {
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};
        ArrayTabulatedFunction func = new ArrayTabulatedFunction(xValues, yValues);

        assertEquals(1, func.indexOfX(1.0));
        assertEquals(2, func.indexOfY(4.0));
        assertEquals(-1, func.indexOfX(3.0));
        assertEquals(-1, func.indexOfY(5.0));
    }

    @Test
    public void testApply() {
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};
        ArrayTabulatedFunction func = new ArrayTabulatedFunction(xValues, yValues);

//        Точные значения
        assertEquals(0.0, func.apply(0.0), 1e-10);
        assertEquals(1.0, func.apply(1.0), 1e-10);
        assertEquals(4.0, func.apply(2.0), 1e-10);

//        Интерполяция
        assertEquals(0.5, func.apply(0.5), 1e-10);
        assertEquals(2.5, func.apply(1.5), 1e-10);

//        Экстраполяция
        assertEquals(-1.0, func.apply(-1.0), 1e-10);
        assertEquals(7.0, func.apply(3.0), 1e-10);
    }
}