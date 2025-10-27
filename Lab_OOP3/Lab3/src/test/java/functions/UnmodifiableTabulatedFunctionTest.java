package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnmodifiableTabulatedFunctionTest {

    @Test
    public void testUnmodifiableFunction() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        ArrayTabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);

        // Проверяем, что чтение работает
        assertEquals(3, unmodifiable.getCount());
        assertEquals(1.0, unmodifiable.getX(0), 1e-10);
        assertEquals(1.0, unmodifiable.getY(0), 1e-10);
        assertEquals(9.0, unmodifiable.apply(3.0), 1e-10);

        // Проверяем, что запись бросает исключение
        assertThrows(UnsupportedOperationException.class, () -> {
            unmodifiable.setY(0, 5.0);
        });
    }

    @Test
    public void testUnmodifiableWithLinkedList() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        LinkedListTabulatedFunction original = new LinkedListTabulatedFunction(xValues, yValues);
        UnmodifiableTabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(original);

        // Проверяем, что чтение работает
        assertEquals(3, unmodifiable.getCount());
        assertEquals(1.0, unmodifiable.getX(0), 1e-10);

        // Проверяем, что запись бросает исключение
        assertThrows(UnsupportedOperationException.class, () -> {
            unmodifiable.setY(0, 5.0);
        });
    }
}


