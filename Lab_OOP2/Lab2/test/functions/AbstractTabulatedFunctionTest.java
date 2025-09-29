package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AbstractTabulatedFunctionTest {

    @Test
    void testInterpolate() {
        // f(0)=0, f(2)=4 - линейная функция f(x)=2x
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 2.0, 0.0, 4.0);

        // Тестируем защищенный метод interpolate через apply
        assertEquals(1.0, function.apply(0.5), 1e-10, "Интерполяция в середине");
        assertEquals(2.0, function.apply(1.0), 1e-10, "Интерполяция в середине");
        assertEquals(3.0, function.apply(1.5), 1e-10, "Интерполяция ближе к правой границе");
    }

    @Test
    void testExtrapolateLeft() {
        MockTabulatedFunction function = new MockTabulatedFunction(1.0, 3.0, 2.0, 8.0);

        // Экстраполяция слева: f(x) = 3x - 1
        assertEquals(-1.0, function.apply(0.0), 1e-10, "Экстраполяция слева");
        assertEquals(-4.0, function.apply(-1.0), 1e-10, "Экстраполяция слева дальше");
    }

    @Test
    void testExtrapolateRight() {
        MockTabulatedFunction function = new MockTabulatedFunction(1.0, 3.0, 2.0, 8.0);

        // Экстраполяция справа: f(x) = 3x - 1
        assertEquals(11.0, function.apply(4.0), 1e-10, "Экстраполяция справа");
        assertEquals(14.0, function.apply(5.0), 1e-10, "Экстраполяция справа дальше");
    }

    @Test
    void testApplyWithExactMatch() {
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 2.0, 0.0, 4.0);

        // Точное совпадение с существующими x
        assertEquals(0.0, function.apply(0.0), 1e-10, "Точное совпадение с x0");
        assertEquals(4.0, function.apply(2.0), 1e-10, "Точное совпадение с x1");
    }

    @Test
    void testApplyWithConstantFunction() {
        // Постоянная функция
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 10.0, 5.0, 5.0);

        assertEquals(5.0, function.apply(-5.0), 1e-10, "Экстраполяция слева константы");
        assertEquals(5.0, function.apply(5.0), 1e-10, "Интерполяция константы");
        assertEquals(5.0, function.apply(15.0), 1e-10, "Экстраполяция справа константы");
    }

    @Test
    void testApplyWithDecreasingFunction() {
        // Убывающая функция
        MockTabulatedFunction function = new MockTabulatedFunction(0.0, 5.0, 10.0, 0.0);

        assertEquals(8.0, function.apply(1.0), 1e-10, "Интерполяция убывающей функции");
        assertEquals(12.0, function.apply(-1.0), 1e-10, "Экстраполяция слева убывающей функции");
        assertEquals(-2.0, function.apply(6.0), 1e-10, "Экстраполяция справа убывающей функции");
    }

    @Test
    void testToString() {
        MockTabulatedFunction function = new MockTabulatedFunction(1.0, 2.0, 3.0, 4.0);
        String expected = "TabulatedFunction[(1.0; 3.0), (2.0; 4.0)]";
        assertEquals(expected, function.toString());
    }

    @Test
    void testFloorIndexOfX() {
        MockTabulatedFunction function = new MockTabulatedFunction(1.0, 3.0, 2.0, 8.0);

        // Метод floorIndexOfX protected, тестируем через apply
        assertEquals(2.0, function.apply(1.0), 1e-10); // На левой границе
        assertEquals(5.0, function.apply(2.0), 1e-10); // Внутри интервала
        assertEquals(8.0, function.apply(3.0), 1e-10); // На правой границе
    }
    @Test
    public void testInterpolate() {
        MockTabulatedFunction mock = new MockTabulatedFunction(0.0, 2.0, 0.0, 4.0);

        // Тестирование защищенного метода interpolate через mock
        // В середине: x=1.0 должен дать y=2.0
        double result = mock.apply(1.0);
        assertEquals(2.0, result, 1e-10);
    }

    @Test
    public void testApplyExtrapolation() {
        MockTabulatedFunction mock = new MockTabulatedFunction(0.0, 2.0, 0.0, 4.0);

        // Экстраполяция слева
        assertEquals(-2.0, mock.apply(-1.0), 1e-10);

        // Экстраполяция справа
        assertEquals(6.0, mock.apply(3.0), 1e-10);
    }

    @Test
    public void testApplyExactValue() {
        MockTabulatedFunction mock = new MockTabulatedFunction(0.0, 2.0, 0.0, 4.0);

        // Точное значение
        assertEquals(0.0, mock.apply(0.0), 1e-10);
        assertEquals(4.0, mock.apply(2.0), 1e-10);
    }

}