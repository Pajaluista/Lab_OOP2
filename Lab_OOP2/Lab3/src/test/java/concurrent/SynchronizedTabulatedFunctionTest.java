package concurrent;

import functions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SynchronizedTabulatedFunctionTest {

    @Test
    public void testBasicOperations() {
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(new UnitFunction(), 0, 10, 11);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        assertEquals(11, syncFunction.getCount());
        assertEquals(0.0, syncFunction.getX(0), 1e-9);
        assertEquals(10.0, syncFunction.getX(10), 1e-9);
        assertEquals(1.0, syncFunction.getY(5), 1e-9);
    }

    @Test
    public void testSetY() {
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(new UnitFunction(), 0, 10, 11);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        syncFunction.setY(5, 42.0);
        assertEquals(42.0, syncFunction.getY(5), 1e-9);
    }

    @Test
    public void testDoSynchronouslyWithReturnValue() {
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(new UnitFunction(), 0, 10, 11);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        Double result = syncFunction.doSynchronously(func -> {
            double sum = 0;
            for (int i = 0; i < func.getCount(); i++) {
                sum += func.getY(i);
            }
            return sum;
        });

        assertEquals(11.0, result, 1e-9); // 11 точек, каждая y=1
    }

    @Test
    public void testDoSynchronouslyWithVoid() {
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(new UnitFunction(), 0, 10, 11);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        syncFunction.doSynchronously(func -> {
            for (int i = 0; i < func.getCount(); i++) {
                func.setY(i, func.getY(i) * 2);
            }
            return null;
        });

        assertEquals(2.0, syncFunction.getY(0), 1e-9);
    }

    @Test
    public void testIterator() {
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(new UnitFunction(), 0, 2, 3);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);

        int pointCount = 0;
        for (Point point : syncFunction) {
            assertNotNull(point);
            pointCount++;
        }
        assertEquals(3, pointCount);
    }
}