package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsertableTest {

    @Test
    void testInsertUpdateExistingValue() {
        // Тестируем замену существующего значения
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};

        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        function.insert(1.0, 10.0); // Заменяем существующее значение

        assertEquals(3, function.getCount());
        assertEquals(10.0, function.getY(1), 1e-10);
        assertEquals(1.0, function.getX(1), 1e-10);
    }

    @Test
    void testInsertAtBeginning() {
        // Тестируем вставку в начало
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};

        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        function.insert(0.0, 0.0); // Вставляем в начало

        assertEquals(4, function.getCount());
        assertEquals(0.0, function.getX(0), 1e-10);
        assertEquals(0.0, function.getY(0), 1e-10);
        assertEquals(0.0, function.leftBound(), 1e-10);
    }

    @Test
    void testInsertAtEnd() {
        // Тестируем вставку в конец
        double[] xValues = {0.0, 1.0, 2.0};
        double[] yValues = {0.0, 1.0, 4.0};

        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        function.insert(3.0, 9.0); // Вставляем в конец

        assertEquals(4, function.getCount());
        assertEquals(3.0, function.getX(3), 1e-10);
        assertEquals(9.0, function.getY(3), 1e-10);
        assertEquals(3.0, function.rightBound(), 1e-10);
    }

    @Test
    void testInsertInMiddle() {
        // Тестируем вставку в середину
        double[] xValues = {0.0, 2.0, 4.0};
        double[] yValues = {0.0, 4.0, 16.0};

        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);
        function.insert(1.0, 1.0); // Вставляем между 0.0 и 2.0

        assertEquals(4, function.getCount());
        assertEquals(1.0, function.getX(1), 1e-10);
        assertEquals(1.0, function.getY(1), 1e-10);

        // Проверяем порядок
        assertEquals(0.0, function.getX(0), 1e-10);
        assertEquals(1.0, function.getX(1), 1e-10);
        assertEquals(2.0, function.getX(2), 1e-10);
        assertEquals(4.0, function.getX(3), 1e-10);
    }

    @Test
    void testInsertIntoEmptyFunction() {
        // Тестируем вставку в пустую функцию (требует модификации конструкторов)
        // Для этого нужно создать специальный конструктор для пустой функции
        // или начать с минимального количества точек
    }

    @Test
    void testInsertLinkedList() {
        // Тестируем вставку для связного списка
        double[] xValues = {0.0, 2.0};
        double[] yValues = {0.0, 4.0};

        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(xValues, yValues);
        function.insert(1.0, 1.0); // Вставляем в середину

        assertEquals(3, function.getCount());
        assertEquals(1.0, function.getX(1), 1e-10);
        assertEquals(1.0, function.getY(1), 1e-10);

        // Проверяем структуру списка
        assertEquals(0.0, function.head.x, 1e-10);
        assertEquals(1.0, function.head.next.x, 1e-10);
        assertEquals(2.0, function.head.next.next.x, 1e-10);
    }

    @Test
    void testInsertMaintainsSorting() {
        // Проверяем, что сортировка сохраняется после множественных вставок
        double[] xValues = {1.0, 4.0};
        double[] yValues = {1.0, 16.0};

        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        function.insert(3.0, 9.0); // Между 1 и 4
        function.insert(0.0, 0.0); // В начало
        function.insert(5.0, 25.0); // В конец
        function.insert(2.0, 4.0); // Между 1 и 3

        // Проверяем порядок
        assertEquals(6, function.getCount());
        assertEquals(0.0, function.getX(0), 1e-10);
        assertEquals(1.0, function.getX(1), 1e-10);
        assertEquals(2.0, function.getX(2), 1e-10);
        assertEquals(3.0, function.getX(3), 1e-10);
        assertEquals(4.0, function.getX(4), 1e-10);
        assertEquals(5.0, function.getX(5), 1e-10);
    }

    @Test
    void testInsertWithInterpolation() {
        // Проверяем, что интерполяция работает корректно после вставки
        double[] xValues = {0.0, 2.0};
        double[] yValues = {0.0, 4.0};

        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        // До вставки: f(1.0) = 2.0 (интерполяция)
        assertEquals(2.0, function.apply(1.0), 1e-10);

        // Вставляем точку в середину
        function.insert(1.0, 1.0);

        // После вставки: f(1.0) = 1.0 (точное значение)
        assertEquals(1.0, function.apply(1.0), 1e-10);

        // Проверяем интерполяцию в новом интервале
        assertEquals(0.5, function.apply(0.5), 1e-10); // Между 0 и 1
        assertEquals(2.5, function.apply(1.5), 1e-10); // Между 1 и 2
    }
}