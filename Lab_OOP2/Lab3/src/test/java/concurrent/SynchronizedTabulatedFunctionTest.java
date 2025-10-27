package concurrent;

import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.Point;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class SynchronizedTabulatedFunctionTest {

    @Test
    void iteratorReturnsAllPointsInCorrectOrder() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {10.0, 20.0, 30.0};
        TabulatedFunction original = new ArrayTabulatedFunction(x, y);
        TabulatedFunction safe = new SynchronizedTabulatedFunction(original);

        Iterator<Point> it = safe.iterator();
        assertTrue(it.hasNext());
        assertEquals(1.0, it.next().x, 1e-10);
        assertEquals(2.0, it.next().x, 1e-10);
        assertEquals(3.0, it.next().x, 1e-10);
        assertFalse(it.hasNext());
    }

    @Test
    void iteratorUsesSnapshotAndIgnoresLaterModifications() {
        double[] x = {1.0, 2.0};
        double[] y = {100.0, 200.0};
        TabulatedFunction original = new ArrayTabulatedFunction(x, y);
        TabulatedFunction safe = new SynchronizedTabulatedFunction(original);

        Iterator<Point> it = safe.iterator();
        Point first = it.next(); // читаем первую точку
        safe.setY(1, -999.0);    // меняем вторую точку
        Point second = it.next(); // читаем вторую точку

        assertEquals(100.0, first.y, 1e-10);
        assertEquals(200.0, second.y, 1e-10); // не -999.0!
    }

    @Test
    void forEachLoopWorks() {
        double[] x = {0.0, 1.0};
        double[] y = {0.0, 1.0};
        TabulatedFunction func = new SynchronizedTabulatedFunction(
                new LinkedListTabulatedFunction(x, y)
        );

        int count = 0;
        for (Point p : func) {
            assertNotNull(p);
            count++;
        }
        assertEquals(2, count);
    }
}