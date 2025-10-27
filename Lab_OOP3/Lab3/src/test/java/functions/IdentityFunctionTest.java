package functions;

public class IdentityFunctionTest {

    public static void main(String[] args) {
        testApply();
    }

    public static void testApply() {
        IdentityFunction identity = new IdentityFunction();

        // Простые проверки без assert
        if (identity.apply(5.0) != 5.0) {
            System.out.println("❌ Тест 1 провален: f(5.0) должно быть 5.0");
            return;
        }

        if (identity.apply(-3.0) != -3.0) {
            System.out.println("❌ Тест 2 провален: f(-3.0) должно быть -3.0");
            return;
        }

        if (identity.apply(0.0) != 0.0) {
            System.out.println("❌ Тест 3 провален: f(0.0) должно быть 0.0");
            return;
        }

        System.out.println("✅ Все тесты IdentityFunction пройдены!");
    }
}