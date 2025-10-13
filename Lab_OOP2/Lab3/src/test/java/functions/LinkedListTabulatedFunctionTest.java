package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

class LinkedListTabulatedFunctionTest {

    @Test
    void testConstructorFromArrays() {
        double[] xValues = {0.0, 1.0, 2.0, 3.0};
        double[] yValues = {0.0, 1.0, 4.0, 9.0};

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        assertEquals(4, function.getCount());
        assertEquals(0.0, function.leftBound(), 1e-10);
        assertEquals(3.0, function.rightBound(), 1e-10);
    }

    @Test
    void testConstructorFromFunction() {
        MathFunction source = new SqrFunction();
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(source, 0, 2, 3);

        assertEquals(3, function.getCount());
        assertEquals(0.0, function.getX(0), 1e-10);
        assertEquals(1.0, function.getX(1), 1e-10);
        assertEquals(2.0, function.getX(2), 1e-10);
    }

    @Test
    void testCircularListStructure() {
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        // Проверяем циклическую структуру
        assertEquals(0.0, function.head.x, 1e-10);
        assertEquals(2.0, function.head.prev.x, 1e-10);
        assertEquals(1.0, function.head.next.x, 1e-10);
    }

    // Дополнительные тесты для всех методов...
    @Test
    void testRemoveMiddleElement() {
        double[] xValues = {0.0, 1.0, 2.0, 3.0};
        double[] yValues = {0.0, 1.0, 4.0, 9.0};

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        assertEquals(4, function.getCount());

        // Удаляем средний элемент (индекс 1 - x=1.0)
        function.remove(1);

        assertEquals(3, function.getCount());
        assertEquals(2.0, function.getX(1), 1e-10);
        assertEquals(4.0, function.getY(1), 1e-10);
    }

    @Test
    void testRemoveFirstElement() {
        double[] xValues = {0.0, 1.0, 2.0, 3.0};
        double[] yValues = {0.0, 1.0, 4.0, 9.0};

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.remove(0); // Удаляем первый элемент

        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(0), 1e-10);
        assertEquals(2.0, function.getX(1), 1e-10);
        assertEquals(3.0, function.getX(2), 1e-10);
    }

    @Test
    void testRemoveLastElement() {
        double[] xValues = {0.0, 1.0, 2.0, 3.0};
        double[] yValues = {0.0, 1.0, 4.0, 9.0};

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        function.remove(3); // Удаляем последний элемент

        assertEquals(3, function.getCount());
        assertEquals(0.0, function.getX(0), 1e-10);
        assertEquals(1.0, function.getX(1), 1e-10);
        assertEquals(2.0, function.getX(2), 1e-10);
    }

    @Test
    void testRemoveShouldFailWhenOnlyTwoPointsLeft() {
        double[] xValues = {0.0, 1.0, 2.0}; // 3 точки
        double[] yValues = {0.0, 1.0, 4.0};

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        // Удаляем одну точку - должно работать
        function.remove(1);
        assertEquals(2, function.getCount());

        // Попытка удалить еще одну точку должна вызвать исключение
        assertThrows(IllegalStateException.class, () -> {
            function.remove(0);
        });
    }

    @Test
    void testRemoveInvalidIndex() {
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        // Отрицательный индекс
        assertThrows(IllegalArgumentException.class, () -> {
            function.remove(-1);
        });

        // Индекс больше размера
        assertThrows(IllegalArgumentException.class, () -> {
            function.remove(5);
        });
    }
    @Test
    public void testIteratorWithWhileLoop() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {4.0, 5.0, 6.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        Iterator<Point> iterator = function.iterator();
        int pointCount = 0;

        while (iterator.hasNext()) {
            Point point = iterator.next();
            assertEquals(xValues[pointCount], point.x, 1e-10);
            assertEquals(yValues[pointCount], point.y, 1e-10);
            pointCount++;
        }

        assertEquals(3, pointCount);

        // Проверяем, что после завершения итератор бросает исключение
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testIteratorWithForEachLoop() {
        double[] xValues = {0.5, 1.5, 2.5};
        double[] yValues = {10.0, 20.0, 30.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        int pointCount = 0;
        for (Point point : function) {
            assertEquals(xValues[pointCount], point.x, 1e-10);
            assertEquals(yValues[pointCount], point.y, 1e-10);
            pointCount++;
        }

        assertEquals(3, pointCount);
    }

    @Test
    public void testIteratorWithEmptyFunction() {
        // Этот тест может не понадобиться, так как конструктор требует минимум 2 точки
        // Но можно протестировать на минимальном размере
        double[] xValues = {1.0, 2.0};
        double[] yValues = {3.0, 4.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        int pointCount = 0;
        for (Point point : function) {
            pointCount++;
        }

        assertEquals(2, pointCount);
    }

    @Test
    public void testIteratorOrder() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        Iterator<Point> iterator = function.iterator();

        // Проверяем порядок точек
        Point first = iterator.next();
        assertEquals(1.0, first.x, 1e-10);
        assertEquals(1.0, first.y, 1e-10);

        Point second = iterator.next();
        assertEquals(2.0, second.x, 1e-10);
        assertEquals(4.0, second.y, 1e-10);

        Point third = iterator.next();
        assertEquals(3.0, third.x, 1e-10);
        assertEquals(9.0, third.y, 1e-10);

        Point fourth = iterator.next();
        assertEquals(4.0, fourth.x, 1e-10);
        assertEquals(16.0, fourth.y, 1e-10);

        assertFalse(iterator.hasNext());
    }
}