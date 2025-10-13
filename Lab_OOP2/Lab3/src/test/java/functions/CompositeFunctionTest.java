package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompositeFunctionTest {

    @Test
    void testSimpleComposition() {
        // f(x) = x + 2, g(x) = x * 3
        // h(x) = g(f(x)) = (x + 2) * 3
        MathFunction f = x -> x + 2;
        MathFunction g = x -> x * 3;
        CompositeFunction h = new CompositeFunction(f, g);

        assertEquals(12.0, h.apply(2.0), 0.0001); // (2 + 2) * 3 = 12
        assertEquals(0.0, h.apply(-2.0), 0.0001); // (-2 + 2) * 3 = 0
    }

    @Test
    void testWithExistingFunctions() {
        // f(x) = x² (SqrFunction), g(x) = √x (через Math.sqrt)
        // h(x) = g(f(x)) = √(x²) = |x|
        MathFunction f = new SqrFunction();
        MathFunction g = Math::sqrt; // g(x) = √x
        CompositeFunction h = new CompositeFunction(f, g);

        assertEquals(3.0, h.apply(3.0), 0.0001);  // √(3²) = 3
        assertEquals(3.0, h.apply(-3.0), 0.0001); // √((-3)²) = 3
        assertEquals(0.0, h.apply(0.0), 0.0001);  // √(0²) = 0
    }

    @Test
    void testIdentityFunctionComposition() {
        // f(x) = x (IdentityFunction), g(x) = x * 5
        // h(x) = g(f(x)) = 5x
        MathFunction f = new IdentityFunction();
        MathFunction g = x -> x * 5;
        CompositeFunction h = new CompositeFunction(f, g);

        assertEquals(10.0, h.apply(2.0), 0.0001);
        assertEquals(-15.0, h.apply(-3.0), 0.0001);
    }

    @Test
    void testNestedComposition() {
        // Создаем сложную композицию: h(x) = k(m(x)), где m(x) = g(f(x))
        // f(x) = x + 1, g(x) = x * 2, k(x) = x²
        // h(x) = k(m(x)) = [(x + 1) * 2]²

        MathFunction f = x -> x + 1;
        MathFunction g = x -> x * 2;
        MathFunction k = new SqrFunction();

        // Сначала создаем m(x) = g(f(x))
        CompositeFunction m = new CompositeFunction(f, g);
        // Затем h(x) = k(m(x))
        CompositeFunction h = new CompositeFunction(m, k);

        assertEquals(16.0, h.apply(1.0), 0.0001);  // [(1 + 1) * 2]² = 16
        assertEquals(0.0, h.apply(-1.0), 0.0001);  // [(-1 + 1) * 2]² = 0
    }

    @Test
    void testCompositionWithItself() {
        // f(x) = x + 1
        // h(x) = f(f(x)) = (x + 1) + 1 = x + 2
        MathFunction f = x -> x + 1;
        CompositeFunction h = new CompositeFunction(f, f);

        assertEquals(4.0, h.apply(2.0), 0.0001);  // 2 + 2 = 4
        assertEquals(0.0, h.apply(-2.0), 0.0001); // -2 + 2 = 0
    }

    @Test
    void testMultipleLevelComposition() {
        // Создаем цепочку: f3(f2(f1(x)))
        MathFunction f1 = x -> x * 2;      // умножить на 2
        MathFunction f2 = x -> x + 3;      // прибавить 3
        MathFunction f3 = new SqrFunction(); // возвести в квадрат

        // f2(f1(x)) = (x * 2) + 3
        CompositeFunction firstLevel = new CompositeFunction(f1, f2);
        // f3(f2(f1(x))) = [(x * 2) + 3]²
        CompositeFunction secondLevel = new CompositeFunction(firstLevel, f3);

        assertEquals(49.0, secondLevel.apply(2.0), 0.0001);  // [(2*2) + 3]² = 49
        assertEquals(1.0, secondLevel.apply(-1.0), 0.0001);  // [(-1*2) + 3]² = 1
    }

    @Test
    void testCompositionWithConstantFunction() {
        // f(x) = x², g(x) = 5 (постоянная функция)
        // h(x) = g(f(x)) = 5 (всегда возвращает 5)
        MathFunction f = new SqrFunction();
        MathFunction g = x -> 5.0; // Constant function
        CompositeFunction h = new CompositeFunction(f, g);

        // Независимо от x, результат всегда 5
        assertEquals(5.0, h.apply(10.0), 0.0001);
        assertEquals(5.0, h.apply(0.0), 0.0001);
        assertEquals(5.0, h.apply(-5.0), 0.0001);
    }
}