package operations;

import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TabulatedDifferentialOperatorTest {

    @Test
    public void testDeriveLinearFunctionWithArrayFactory() {
        // Arrange: f(x) = 2x + 1, производная должна быть 2
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {3.0, 5.0, 7.0, 9.0}; // 2x + 1

        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factory);
        TabulatedFunction function = factory.create(xValues, yValues);

        // Act
        TabulatedFunction derivative = operator.derive(function);

        // Assert
        assertNotNull(derivative);
        assertTrue(derivative instanceof ArrayTabulatedFunction);
        assertEquals(4, derivative.getCount());

        // Производная линейной функции постоянна и равна 2
        for (int i = 0; i < derivative.getCount(); i++) {
            assertEquals(2.0, derivative.getY(i), 1e-10,
                    "Производная линейной функции должна быть постоянной");
        }
    }

    @Test
    public void testDeriveQuadraticFunctionWithLinkedListFactory() {
        // Arrange: f(x) = x², производная должна быть 2x
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0}; // x²

        TabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(factory);
        TabulatedFunction function = factory.create(xValues, yValues);

        // Act
        TabulatedFunction derivative = operator.derive(function);

        // Assert
        assertNotNull(derivative);
        assertTrue(derivative instanceof LinkedListTabulatedFunction);
        assertEquals(4, derivative.getCount());

        // Проверяем значения производной: f'(x) = 2x
        assertEquals(2.0, derivative.getY(0), 1e-10); // f'(1) = 2
        assertEquals(4.0, derivative.getY(1), 1e-10); // f'(2) = 4
        assertEquals(6.0, derivative.getY(2), 1e-10); // f'(3) = 6
        assertEquals(6.0, derivative.getY(3), 1e-10); // Последняя точка тоже 6 (левая производная)
    }

    @Test
    public void testDefaultConstructorUsesArrayFactory() {
        // Arrange
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
        double[] xValues = {1.0, 2.0};
        double[] yValues = {1.0, 2.0};
        TabulatedFunction function = operator.getFactory().create(xValues, yValues);

        // Act
        TabulatedFunction derivative = operator.derive(function);

        // Assert
        assertNotNull(derivative);
        assertTrue(derivative instanceof ArrayTabulatedFunction);
    }

    @Test
    public void testFactoryGetterAndSetter() {
        // Arrange
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
        TabulatedFunctionFactory originalFactory = operator.getFactory();

        // Act & Assert - проверяем геттер
        assertNotNull(originalFactory);
        assertTrue(originalFactory instanceof ArrayTabulatedFunctionFactory);

        // Act & Assert - проверяем сеттер
        TabulatedFunctionFactory newFactory = new LinkedListTabulatedFunctionFactory();
        operator.setFactory(newFactory);
        assertSame(newFactory, operator.getFactory());
    }

    @Test
    public void testDeriveWithDifferentStepSizes() {
        // Arrange: неравномерная сетка
        double[] xValues = {1.0, 1.5, 3.0, 3.2}; // неравномерный шаг
        double[] yValues = {1.0, 2.25, 9.0, 10.24}; // x²

        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
        TabulatedFunction function = operator.getFactory().create(xValues, yValues);

        // Act
        TabulatedFunction derivative = operator.derive(function);

        // Assert
        assertEquals(4, derivative.getCount());

        // Проверяем, что x значения сохранились
        for (int i = 0; i < derivative.getCount(); i++) {
            assertEquals(xValues[i], derivative.getX(i), 1e-10);
        }

        // Проверяем приближенные значения производной
        assertEquals(2.5, derivative.getY(0), 0.1); // (2.25-1.0)/(1.5-1.0) = 2.5 ≈ f'(1)=2
        assertEquals(4.5, derivative.getY(1), 0.1); // (9.0-2.25)/(3.0-1.5) = 4.5 ≈ f'(2)=4
        assertEquals(6.2, derivative.getY(2), 0.1); // (10.24-9.0)/(3.2-3.0) = 6.2 ≈ f'(3)=6
        assertEquals(6.2, derivative.getY(3), 0.1); // последняя точка такая же как предпоследняя
    }

    @Test
    public void testDeriveConstantFunction() {
        // Arrange: f(x) = 5, производная должна быть 0
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {5.0, 5.0, 5.0};

        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
        TabulatedFunction function = operator.getFactory().create(xValues, yValues);

        // Act
        TabulatedFunction derivative = operator.derive(function);

        // Assert
        for (int i = 0; i < derivative.getCount(); i++) {
            assertEquals(0.0, derivative.getY(i), 1e-10,
                    "Производная константы должна быть 0");
        }
    }
}