package operations;

import exceptions.InconsistentFunctionsException;
import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

public class TabulatedFunctionOperationServiceTest {

    @Test
    public void testAsPointsWithArrayTabulatedFunction() {
        // Arrange
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Act
        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        // Assert
        assertNotNull(points);
        assertEquals(4, points.length);

        for (int i = 0; i < points.length; i++) {
            assertEquals(xValues[i], points[i].x, 1e-10);
            assertEquals(yValues[i], points[i].y, 1e-10);
        }
    }

    @Test
    public void testAsPointsWithLinkedListTabulatedFunction() {
        // Arrange
        double[] xValues = {0.5, 1.5, 2.5};
        double[] yValues = {5.0, 15.0, 25.0};
        TabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);

        // Act
        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        // Assert
        assertNotNull(points);
        assertEquals(3, points.length);

        for (int i = 0; i < points.length; i++) {
            assertEquals(xValues[i], points[i].x, 1e-10);
            assertEquals(yValues[i], points[i].y, 1e-10);
        }
    }

    @Test
    public void testAsPointsWithSinglePointFunction() {
        // Arrange - минимальная функция (2 точки)
        double[] xValues = {1.0, 2.0};
        double[] yValues = {10.0, 20.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Act
        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        // Assert
        assertEquals(2, points.length);
        assertEquals(1.0, points[0].x, 1e-10);
        assertEquals(10.0, points[0].y, 1e-10);
        assertEquals(2.0, points[1].x, 1e-10);
        assertEquals(20.0, points[1].y, 1e-10);
    }

    @Test
    public void testAsPointsOrderPreservation() {
        // Arrange
        double[] xValues = {3.0, 1.0, 2.0}; // неупорядоченные x
        double[] yValues = {30.0, 10.0, 20.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Act
        Point[] points = TabulatedFunctionOperationService.asPoints(function);

        // Assert - порядок должен сохраниться как в исходной функции
        assertEquals(3, points.length);
        assertEquals(3.0, points[0].x, 1e-10);
        assertEquals(1.0, points[1].x, 1e-10);
        assertEquals(2.0, points[2].x, 1e-10);
    }

    @Test
    public void testAsPointsWithNullFunction() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TabulatedFunctionOperationService.asPoints(null)
        );

        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    public void testAsPointsReturnsIndependentArray() {
        // Arrange
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0, 2.0};
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // Act
        Point[] points1 = TabulatedFunctionOperationService.asPoints(function);
        Point[] points2 = TabulatedFunctionOperationService.asPoints(function);

        // Assert - должны возвращаться разные массивы
        assertNotSame(points1, points2);

        // Но содержимое должно быть одинаковым
        assertEquals(points1.length, points2.length);
        for (int i = 0; i < points1.length; i++) {
            assertEquals(points1[i].x, points2[i].x, 1e-10);
            assertEquals(points1[i].y, points2[i].y, 1e-10);
        }
    }

    @Test
    public void testAddFunctionsWithSameType() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValuesA = {2.0, 3.0, 4.0, 5.0};
        double[] yValuesB = {3.0, 4.0, 5.0, 6.0};

        TabulatedFunction a = new ArrayTabulatedFunction(xValues, yValuesA);
        TabulatedFunction b = new ArrayTabulatedFunction(xValues, yValuesB);

        // Act
        TabulatedFunction result = service.add(a, b);

        // Assert
        assertEquals(4, result.getCount());
        assertEquals(5.0, result.getY(0), 1e-10);  // 2 + 3 = 5
        assertEquals(7.0, result.getY(1), 1e-10);  // 3 + 4 = 7
        assertEquals(9.0, result.getY(2), 1e-10);  // 4 + 5 = 9
        assertEquals(11.0, result.getY(3), 1e-10); // 5 + 6 = 11
    }

    @Test
    public void testAddFunctionsWithDifferentTypes() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValuesA = {1.0, 2.0, 3.0};
        double[] yValuesB = {4.0, 5.0, 6.0};

        TabulatedFunction a = new ArrayTabulatedFunction(xValues, yValuesA); // Array
        TabulatedFunction b = new LinkedListTabulatedFunction(xValues, yValuesB); // LinkedList

        // Act
        TabulatedFunction result = service.add(a, b);

        // Assert
        assertEquals(3, result.getCount());
        assertEquals(5.0, result.getY(0), 1e-10); // 1 + 4 = 5
        assertEquals(7.0, result.getY(1), 1e-10); // 2 + 5 = 7
        assertEquals(9.0, result.getY(2), 1e-10); // 3 + 6 = 9
    }

    @Test
    public void testSubtractFunctions() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValuesA = {5.0, 10.0, 15.0};
        double[] yValuesB = {2.0, 3.0, 4.0};

        TabulatedFunction a = new ArrayTabulatedFunction(xValues, yValuesA);
        TabulatedFunction b = new LinkedListTabulatedFunction(xValues, yValuesB);

        // Act
        TabulatedFunction result = service.subtract(a, b);

        // Assert
        assertEquals(3, result.getCount());
        assertEquals(3.0, result.getY(0), 1e-10); // 5 - 2 = 3
        assertEquals(7.0, result.getY(1), 1e-10); // 10 - 3 = 7
        assertEquals(11.0, result.getY(2), 1e-10); // 15 - 4 = 11
    }

    @Test
    public void testMultiplyFunctions() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValuesA = {2.0, 3.0, 4.0, 5.0};
        double[] yValuesB = {3.0, 4.0, 5.0, 6.0};

        TabulatedFunction a = service.getFactory().create(xValues, yValuesA);
        TabulatedFunction b = service.getFactory().create(xValues, yValuesB);

        // Act
        TabulatedFunction result = service.multiply(a, b);

        // Assert
        assertEquals(4, result.getCount());
        assertEquals(6.0, result.getY(0), 1e-10);  // 2 * 3 = 6
        assertEquals(12.0, result.getY(1), 1e-10); // 3 * 4 = 12
        assertEquals(20.0, result.getY(2), 1e-10); // 4 * 5 = 20
        assertEquals(30.0, result.getY(3), 1e-10); // 5 * 6 = 30
    }

    @Test
    public void testMultiplyWithLinkedListFactory() {
        // Arrange
        TabulatedFunctionOperationService service =
                new TabulatedFunctionOperationService(new LinkedListTabulatedFunctionFactory());
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValuesA = {1.0, 0.0, -1.0};
        double[] yValuesB = {2.0, 3.0, 4.0};

        TabulatedFunction a = service.getFactory().create(xValues, yValuesA);
        TabulatedFunction b = service.getFactory().create(xValues, yValuesB);

        // Act
        TabulatedFunction result = service.multiply(a, b);

        // Assert
        assertEquals(3, result.getCount());
        assertEquals(2.0, result.getY(0), 1e-10);  // 1 * 2 = 2
        assertEquals(0.0, result.getY(1), 1e-10);  // 0 * 3 = 0
        assertEquals(-4.0, result.getY(2), 1e-10); // -1 * 4 = -4
    }

    @Test
    public void testDivideFunctions() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValuesA = {6.0, 12.0, 20.0, 30.0};
        double[] yValuesB = {2.0, 3.0, 4.0, 5.0};

        TabulatedFunction a = service.getFactory().create(xValues, yValuesA);
        TabulatedFunction b = service.getFactory().create(xValues, yValuesB);

        // Act
        TabulatedFunction result = service.divide(a, b);

        // Assert
        assertEquals(4, result.getCount());
        assertEquals(3.0, result.getY(0), 1e-10);  // 6 / 2 = 3
        assertEquals(4.0, result.getY(1), 1e-10);  // 12 / 3 = 4
        assertEquals(5.0, result.getY(2), 1e-10);  // 20 / 4 = 5
        assertEquals(6.0, result.getY(3), 1e-10);  // 30 / 5 = 6
    }

    @Test
    public void testDivideByZeroThrowsException() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValuesA = {1.0, 2.0, 3.0};
        double[] yValuesB = {1.0, 0.0, 1.0}; // деление на ноль во второй точке

        TabulatedFunction a = service.getFactory().create(xValues, yValuesA);
        TabulatedFunction b = service.getFactory().create(xValues, yValuesB);

        // Act & Assert
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            service.divide(a, b);
        });

        assertTrue(exception.getMessage().contains("Division by zero"));
    }

    @Test
    public void testDivideWithNegativeNumbers() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {-2.0, -1.0, 0.0, 1.0, 2.0};
        double[] yValuesA = {4.0, 1.0, 0.0, 1.0, 4.0}; // x²
        double[] yValuesB = {-2.0, -1.0, 1.0, 1.0, 2.0}; // x

        TabulatedFunction a = service.getFactory().create(xValues, yValuesA);
        TabulatedFunction b = service.getFactory().create(xValues, yValuesB);

        // Act
        TabulatedFunction result = service.divide(a, b);

        // Assert
        assertEquals(5, result.getCount());
        assertEquals(-2.0, result.getY(0), 1e-10); // 4 / -2 = -2
        assertEquals(-1.0, result.getY(1), 1e-10); // 1 / -1 = -1
        assertEquals(0.0, result.getY(2), 1e-10);  // 0 / 1 = 0
        assertEquals(1.0, result.getY(3), 1e-10);  // 1 / 1 = 1
        assertEquals(2.0, result.getY(4), 1e-10);  // 4 / 2 = 2
    }

    @Test
    public void testInconsistentFunctionsDifferentCount() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValuesA = {1.0, 2.0, 3.0};
        double[] xValuesB = {1.0, 2.0}; // разное количество точек

        TabulatedFunction a = new ArrayTabulatedFunction(xValuesA, new double[]{1, 2, 3});
        TabulatedFunction b = new ArrayTabulatedFunction(xValuesB, new double[]{1, 2});

        // Act & Assert
        InconsistentFunctionsException exception = assertThrows(
                InconsistentFunctionsException.class,
                () -> service.add(a, b)
        );

        assertTrue(exception.getMessage().contains("different number of points"));
    }

    @Test
    public void testInconsistentFunctionsDifferentXValues() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValuesA = {1.0, 2.0, 3.0};
        double[] xValuesB = {1.0, 2.5, 3.0}; // разные x значения

        TabulatedFunction a = new ArrayTabulatedFunction(xValuesA, new double[]{1, 2, 3});
        TabulatedFunction b = new ArrayTabulatedFunction(xValuesB, new double[]{1, 2, 3});

        // Act & Assert
        InconsistentFunctionsException exception = assertThrows(
                InconsistentFunctionsException.class,
                () -> service.add(a, b)
        );

        assertTrue(exception.getMessage().contains("x values are inconsistent"));
    }

    @Test
    public void testInconsistentFunctionsForMultiply() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValuesA = {1.0, 2.0, 3.0};
        double[] xValuesB = {1.0, 2.5, 3.0}; // несовместимые x значения

        TabulatedFunction a = service.getFactory().create(xValuesA, new double[]{1, 2, 3});
        TabulatedFunction b = service.getFactory().create(xValuesB, new double[]{1, 2, 3});

        // Act & Assert
        assertThrows(InconsistentFunctionsException.class, () -> {
            service.multiply(a, b);
        });
    }

    @Test
    public void testInconsistentFunctionsForDivide() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValuesA = {1.0, 2.0};
        double[] xValuesB = {1.0, 3.0}; // разные x значения

        TabulatedFunction a = service.getFactory().create(xValuesA, new double[]{1, 2});
        TabulatedFunction b = service.getFactory().create(xValuesB, new double[]{1, 2});

        // Act & Assert
        assertThrows(InconsistentFunctionsException.class, () -> {
            service.divide(a, b);
        });
    }

    @Test
    public void testFactoryGetterAndSetter() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        TabulatedFunctionFactory originalFactory = service.getFactory();

        // Assert - проверяем геттер
        assertNotNull(originalFactory);
        assertTrue(originalFactory instanceof ArrayTabulatedFunctionFactory);

        // Act & Assert - проверяем сеттер
        TabulatedFunctionFactory newFactory = new LinkedListTabulatedFunctionFactory();
        service.setFactory(newFactory);
        assertSame(newFactory, service.getFactory());
    }

    @Test
    public void testOperationsWithLinkedListFactory() {
        // Arrange
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(factory);

        double[] xValues = {1.0, 2.0};
        double[] yValuesA = {10.0, 20.0};
        double[] yValuesB = {5.0, 15.0};

        TabulatedFunction a = factory.create(xValues, yValuesA);
        TabulatedFunction b = factory.create(xValues, yValuesB);

        // Act
        TabulatedFunction addResult = service.add(a, b);
        TabulatedFunction subtractResult = service.subtract(a, b);
        TabulatedFunction multiplyResult = service.multiply(a, b);
        TabulatedFunction divideResult = service.divide(a, b);

        // Assert
        assertTrue(addResult instanceof LinkedListTabulatedFunction);
        assertTrue(subtractResult instanceof LinkedListTabulatedFunction);
        assertTrue(multiplyResult instanceof LinkedListTabulatedFunction);
        assertTrue(divideResult instanceof LinkedListTabulatedFunction);

        assertEquals(15.0, addResult.getY(0), 1e-10); // 10 + 5 = 15
        assertEquals(35.0, addResult.getY(1), 1e-10); // 20 + 15 = 35

        assertEquals(5.0, subtractResult.getY(0), 1e-10); // 10 - 5 = 5
        assertEquals(5.0, subtractResult.getY(1), 1e-10); // 20 - 15 = 5

        assertEquals(50.0, multiplyResult.getY(0), 1e-10); // 10 * 5 = 50
        assertEquals(300.0, multiplyResult.getY(1), 1e-10); // 20 * 15 = 300

        assertEquals(2.0, divideResult.getY(0), 1e-10); // 10 / 5 = 2
        assertEquals(1.3333333333, divideResult.getY(1), 1e-8); // 20 / 15 ≈ 1.333
    }

    @Test
    public void testDefaultConstructorUsesArrayFactory() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        // Act
        TabulatedFunctionFactory factory = service.getFactory();

        // Assert
        assertNotNull(factory);
        assertTrue(factory instanceof ArrayTabulatedFunctionFactory);
    }

    @Test
    public void testConstructorWithFactory() {
        // Arrange
        TabulatedFunctionFactory expectedFactory = new LinkedListTabulatedFunctionFactory();

        // Act
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(expectedFactory);

        // Assert
        assertSame(expectedFactory, service.getFactory());
    }

    @Test
    public void testAllOperationsWithMixedFunctionTypes() {
        // Arrange
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValuesA = {2.0, 4.0, 6.0};
        double[] yValuesB = {1.0, 2.0, 3.0};

        // Array и LinkedList функции
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValuesA);
        TabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(xValues, yValuesB);

        // Act & Assert - все операции должны работать с разными типами
        assertDoesNotThrow(() -> {
            TabulatedFunction addResult = service.add(arrayFunc, linkedListFunc);
            TabulatedFunction subtractResult = service.subtract(arrayFunc, linkedListFunc);
            TabulatedFunction multiplyResult = service.multiply(arrayFunc, linkedListFunc);
            TabulatedFunction divideResult = service.divide(arrayFunc, linkedListFunc);

            // Проверяем результаты
            assertEquals(3.0, addResult.getY(0), 1e-10); // 2 + 1 = 3
            assertEquals(1.0, subtractResult.getY(0), 1e-10); // 2 - 1 = 1
            assertEquals(2.0, multiplyResult.getY(0), 1e-10); // 2 * 1 = 2
            assertEquals(2.0, divideResult.getY(0), 1e-10); // 2 / 1 = 2
        });
    }
}