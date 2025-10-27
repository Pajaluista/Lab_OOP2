package functions.factory;

import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TabulatedFunctionFactoryTest {

    @Test
    public void testArrayTabulatedFunctionFactory() {
        // Arrange
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {10.0, 20.0, 30.0, 40.0};

        // Act
        TabulatedFunction function = factory.create(xValues, yValues);

        // Assert
        assertNotNull(function, "Фабрика должна создавать не-null объект");
        assertTrue(function instanceof ArrayTabulatedFunction,
                "Созданная функция должна быть типа ArrayTabulatedFunction");

        // Проверяем, что функция работает корректно
        assertEquals(4, function.getCount());
        assertEquals(1.0, function.leftBound(), 1e-10);
        assertEquals(4.0, function.rightBound(), 1e-10);
        assertEquals(20.0, function.getY(1), 1e-10);
    }

    @Test
    public void testLinkedListTabulatedFunctionFactory() {
        // Arrange
        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        double[] xValues = {0.5, 1.5, 2.5};
        double[] yValues = {5.0, 15.0, 25.0};

        // Act
        TabulatedFunction function = factory.create(xValues, yValues);

        // Assert
        assertNotNull(function, "Фабрика должна создавать не-null объект");
        assertTrue(function instanceof LinkedListTabulatedFunction,
                "Созданная функция должна быть типа LinkedListTabulatedFunction");

        // Проверяем, что функция работает корректно
        assertEquals(3, function.getCount());
        assertEquals(0.5, function.leftBound(), 1e-10);
        assertEquals(2.5, function.rightBound(), 1e-10);
        assertEquals(15.0, function.getY(1), 1e-10);
    }

    @Test
    public void testDifferentFactoriesCreateDifferentTypes() {
        // Arrange
        TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0, 2.0};

        // Act
        TabulatedFunction arrayFunction = arrayFactory.create(xValues, yValues);
        TabulatedFunction linkedListFunction = linkedListFactory.create(xValues, yValues);

        // Assert
        assertTrue(arrayFunction instanceof ArrayTabulatedFunction,
                "Первая фабрика должна создавать ArrayTabulatedFunction");
        assertTrue(linkedListFunction instanceof LinkedListTabulatedFunction,
                "Вторая фабрика должна создавать LinkedListTabulatedFunction");

        // Проверяем, что это разные типы объектов
        assertNotSame(arrayFunction.getClass(), linkedListFunction.getClass());

        // Но функциональность должна быть одинаковой
        assertEquals(arrayFunction.getCount(), linkedListFunction.getCount());
        assertEquals(arrayFunction.leftBound(), linkedListFunction.leftBound(), 1e-10);
        assertEquals(arrayFunction.rightBound(), linkedListFunction.rightBound(), 1e-10);
    }

    @Test
    public void testFactoryWithDifferentData() {
        // Arrange
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {-2.0, -1.0, 0.0, 1.0, 2.0};
        double[] yValues = {4.0, 1.0, 0.0, 1.0, 4.0}; // x²

        // Act
        TabulatedFunction function = factory.create(xValues, yValues);

        // Assert
        assertNotNull(function);
        assertEquals(5, function.getCount());
        assertEquals(-2.0, function.leftBound(), 1e-10);
        assertEquals(2.0, function.rightBound(), 1e-10);
        assertEquals(4.0, function.getY(0), 1e-10); // f(-2) = 4
        assertEquals(0.0, function.getY(2), 1e-10); // f(0) = 0
    }
}