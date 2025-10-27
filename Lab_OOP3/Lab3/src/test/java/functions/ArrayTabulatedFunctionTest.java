package functions;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayTabulatedFunctionTest  {

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
    @Test
    public void testIteratorWithWhileLoop() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {4.0, 5.0, 6.0, 7.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        Iterator<Point> iterator = function.iterator();
        int pointCount = 0;

        // Тестируем с помощью while цикла
        while (iterator.hasNext()) {
            Point point = iterator.next();
            assertEquals(xValues[pointCount], point.x, 1e-10);
            assertEquals(yValues[pointCount], point.y, 1e-10);
            pointCount++;
        }

        assertEquals(4, pointCount);

        // Проверяем, что после завершения итератор бросает исключение
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testIteratorWithForEachLoop() {
        double[] xValues = {0.5, 1.5, 2.5};
        double[] yValues = {10.0, 20.0, 30.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        int pointCount = 0;

        // Тестируем с помощью for-each цикла
        for (Point point : function) {
            assertEquals(xValues[pointCount], point.x, 1e-10);
            assertEquals(yValues[pointCount], point.y, 1e-10);
            pointCount++;
        }

        assertEquals(3, pointCount);
    }

    @Test
    public void testIteratorOrder() {
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

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

    @Test
    public void testEmptyIterator() {
        // Создаем функцию с минимальным количеством точек (2)
        double[] xValues = {1.0, 2.0};
        double[] yValues = {3.0, 4.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        Iterator<Point> iterator = function.iterator();

        // Должны быть 2 точки
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleIteratorsIndependent() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {2.0, 4.0, 6.0};
        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Создаем два независимых итератора
        Iterator<Point> iterator1 = function.iterator();
        Iterator<Point> iterator2 = function.iterator();

        // Работаем с первым итератором
        Point point1 = iterator1.next();
        assertEquals(1.0, point1.x, 1e-10);

        // Второй итератор должен начать с начала
        Point point2 = iterator2.next();
        assertEquals(1.0, point2.x, 1e-10);

        // Продолжаем с первым итератором
        point1 = iterator1.next();
        assertEquals(2.0, point1.x, 1e-10);

        // Второй итератор все еще на первой точке
        point2 = iterator2.next();
        assertEquals(2.0, point2.x, 1e-10);
    }
}