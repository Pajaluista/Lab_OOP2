package operations;

import functions.MathFunction;
import functions.SqrFunction;
import functions.IdentityFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SteppingDifferentialOperatorTest {

    @Test
    public void testSteppingDifferentialOperatorInvalidStep() {
        // Проверка некорректных шагов
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(0));
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(-1.0));
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new LeftSteppingDifferentialOperator(Double.NaN));
    }

    @Test
    public void testLeftSteppingDifferentialOperatorWithSqrFunction() {
        // Arrange: f(x) = x², производная f'(x) = 2x
        double step = 0.001;
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(step);
        MathFunction sqrFunction = new SqrFunction();

        // Act
        MathFunction derivative = operator.derive(sqrFunction);

        // Assert: проверяем в нескольких точках
        assertEquals(2.0, derivative.apply(1.0), 0.01);  // f'(1) ≈ 2
        assertEquals(4.0, derivative.apply(2.0), 0.01);  // f'(2) ≈ 4
        assertEquals(6.0, derivative.apply(3.0), 0.01);  // f'(3) ≈ 6
    }

    @Test
    public void testRightSteppingDifferentialOperatorWithSqrFunction() {
        // Arrange: f(x) = x², производная f'(x) = 2x
        double step = 0.001;
        RightSteppingDifferentialOperator operator = new RightSteppingDifferentialOperator(step);
        MathFunction sqrFunction = new SqrFunction();

        // Act
        MathFunction derivative = operator.derive(sqrFunction);

        // Assert: проверяем в нескольких точках
        assertEquals(2.0, derivative.apply(1.0), 0.01);  // f'(1) ≈ 2
        assertEquals(4.0, derivative.apply(2.0), 0.01);  // f'(2) ≈ 4
        assertEquals(6.0, derivative.apply(3.0), 0.01);  // f'(3) ≈ 6
    }

    @Test
    public void testMiddleSteppingDifferentialOperatorWithSqrFunction() {
        // Arrange: f(x) = x², производная f'(x) = 2x
        double step = 0.001;
        MiddleSteppingDifferentialOperator operator = new MiddleSteppingDifferentialOperator(step);
        MathFunction sqrFunction = new SqrFunction();

        // Act
        MathFunction derivative = operator.derive(sqrFunction);

        // Assert: средняя производная обычно точнее
        assertEquals(2.0, derivative.apply(1.0), 0.001);  // f'(1) ≈ 2
        assertEquals(4.0, derivative.apply(2.0), 0.001);  // f'(2) ≈ 4
        assertEquals(6.0, derivative.apply(3.0), 0.001);  // f'(3) ≈ 6
    }

    @Test
    public void testLeftSteppingDifferentialOperatorWithIdentityFunction() {
        // Arrange: f(x) = x, производная f'(x) = 1
        double step = 0.1;
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(step);
        MathFunction identityFunction = new IdentityFunction();

        // Act
        MathFunction derivative = operator.derive(identityFunction);

        // Assert: производная тождественной функции всегда 1
        assertEquals(1.0, derivative.apply(0.0), 1e-10);
        assertEquals(1.0, derivative.apply(5.0), 1e-10);
        assertEquals(1.0, derivative.apply(-3.0), 1e-10);
    }

    @Test
    public void testStepGetterAndSetter() {
        // Arrange
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(0.5);

        // Assert - проверяем геттер
        assertEquals(0.5, operator.getStep(), 1e-10);

        // Act & Assert - проверяем сеттер
        operator.setStep(0.25);
        assertEquals(0.25, operator.getStep(), 1e-10);

        // Проверяем, что сеттер тоже валидирует шаг
        assertThrows(IllegalArgumentException.class, () -> operator.setStep(-1.0));
    }

    @Test
    public void testDifferentStepSizes() {
        // Проверяем влияние размера шага на точность
        MathFunction sqrFunction = new SqrFunction();

        // Меньший шаг - выше точность
        LeftSteppingDifferentialOperator operatorSmallStep = new LeftSteppingDifferentialOperator(0.0001);
        MathFunction derivativeSmallStep = operatorSmallStep.derive(sqrFunction);

        // Больший шаг - ниже точность
        LeftSteppingDifferentialOperator operatorLargeStep = new LeftSteppingDifferentialOperator(0.1);
        MathFunction derivativeLargeStep = operatorLargeStep.derive(sqrFunction);

        // Производная в точке x=2 должна быть ≈4
        double exactValue = 4.0;
        double errorSmallStep = Math.abs(derivativeSmallStep.apply(2.0) - exactValue);
        double errorLargeStep = Math.abs(derivativeLargeStep.apply(2.0) - exactValue);

        // Ошибка с меньшим шагом должна быть меньше
        assertTrue(errorSmallStep < errorLargeStep);
    }

    @Test
    public void testDifferentOperatorsAccuracy() {
        // Сравниваем точность разных операторов
        double step = 0.1;
        MathFunction sqrFunction = new SqrFunction();
        double exactValue = 4.0; // f'(2) = 4

        LeftSteppingDifferentialOperator leftOperator = new LeftSteppingDifferentialOperator(step);
        RightSteppingDifferentialOperator rightOperator = new RightSteppingDifferentialOperator(step);
        MiddleSteppingDifferentialOperator middleOperator = new MiddleSteppingDifferentialOperator(step);

        double leftError = Math.abs(leftOperator.derive(sqrFunction).apply(2.0) - exactValue);
        double rightError = Math.abs(rightOperator.derive(sqrFunction).apply(2.0) - exactValue);
        double middleError = Math.abs(middleOperator.derive(sqrFunction).apply(2.0) - exactValue);

        // Средняя производная обычно точнее односторонних
        assertTrue(middleError < leftError);
        assertTrue(middleError < rightError);
    }
}