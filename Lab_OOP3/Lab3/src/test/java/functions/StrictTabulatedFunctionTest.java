package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

public class StrictTabulatedFunctionTest {

    @Test
    public void testStrictArrayTabulatedFunction() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};

        ArrayTabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        StrictTabulatedFunction strictFunction = new StrictTabulatedFunction(arrayFunction);

        // Проверяем делегирование методов
        assertEquals(4, strictFunction.getCount());
        assertEquals(1.0, strictFunction.leftBound(), 1e-10);
        assertEquals(4.0, strictFunction.rightBound(), 1e-10);
        assertEquals(2.0, strictFunction.getX(1), 1e-10);
        assertEquals(4.0, strictFunction.getY(1), 1e-10);

        // Проверяем точные значения - должны работать
        assertEquals(1.0, strictFunction.apply(1.0), 1e-10);
        assertEquals(9.0, strictFunction.apply(3.0), 1e-10);

        // Проверяем интерполяцию - должна бросать исключение
        assertThrows(UnsupportedOperationException.class, () -> strictFunction.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> strictFunction.apply(0.5));
        assertThrows(UnsupportedOperationException.class, () -> strictFunction.apply(4.5));
    }

    @Test
    public void testStrictLinkedListTabulatedFunction() {
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};

        LinkedListTabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(xValues, yValues);
        StrictTabulatedFunction strictFunction = new StrictTabulatedFunction(linkedListFunction);

        // Проверяем делегирование методов
        assertEquals(3, strictFunction.getCount());
        assertEquals(0.0, strictFunction.leftBound(), 1e-10);
        assertEquals(2.0, strictFunction.rightBound(), 1e-10);

        // Проверяем точные значения - должны работать
        assertEquals(0.0, strictFunction.apply(0.0), 1e-10);
        assertEquals(1.0, strictFunction.apply(1.0), 1e-10);
        assertEquals(4.0, strictFunction.apply(2.0), 1e-10);

        // Проверяем интерполяцию - должна бросать исключение
        assertThrows(UnsupportedOperationException.class, () -> strictFunction.apply(0.5));
        assertThrows(UnsupportedOperationException.class, () -> strictFunction.apply(1.7));
    }

    @Test
    public void testIteratorDelegation() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        ArrayTabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        StrictTabulatedFunction strictFunction = new StrictTabulatedFunction(arrayFunction);

        // Проверяем, что итератор делегируется корректно
        Iterator<Point> iterator = strictFunction.iterator();
        assertTrue(iterator.hasNext());

        int pointCount = 0;
        for (Point point : strictFunction) {
            assertEquals(xValues[pointCount], point.x, 1e-10);
            assertEquals(yValues[pointCount], point.y, 1e-10);
            pointCount++;
        }
        assertEquals(3, pointCount);
    }

    @Test
    public void testSetYDelegation() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 2.0, 3.0};

        ArrayTabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        StrictTabulatedFunction strictFunction = new StrictTabulatedFunction(arrayFunction);

        // Проверяем, что setY делегируется корректно
        strictFunction.setY(1, 100.0);
        assertEquals(100.0, strictFunction.getY(1), 1e-10);
        assertEquals(100.0, arrayFunction.getY(1), 1e-10);
    }

    @Test
    public void testIndexOfMethodsDelegation() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {10.0, 20.0, 30.0};

        ArrayTabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        StrictTabulatedFunction strictFunction = new StrictTabulatedFunction(arrayFunction);

        // Проверяем делегирование indexOfX
        assertEquals(1, strictFunction.indexOfX(2.0));
        assertEquals(-1, strictFunction.indexOfX(5.0));

        // Проверяем делегирование indexOfY
        assertEquals(1, strictFunction.indexOfY(20.0));
        assertEquals(-1, strictFunction.indexOfY(50.0));
    }
}