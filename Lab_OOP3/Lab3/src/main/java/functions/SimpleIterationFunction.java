package functions;

public class SimpleIterationFunction implements MathFunction {
    private final MathFunction function;
    private final double initialGuess;
    private final int maxIterations;
    private final double tolerance;

    public SimpleIterationFunction(MathFunction function, double initialGuess, int maxIterations, double tolerance) {
        this.function = function;
        this.initialGuess = initialGuess;
        this.maxIterations = maxIterations;
        this.tolerance = tolerance;
    }

    public SimpleIterationFunction(MathFunction function, double initialGuess) {
        this(function, initialGuess, 1000, 1e-10);
    }

    @Override
    public double apply(double x) {
        // Метод простых итераций для нахождения корня
        double current = initialGuess;

        for (int i = 0; i < maxIterations; i++) {
            double next = function.apply(current);

            // Проверяем сходимость
            if (Math.abs(next - current) < tolerance) {
                return next; // Найден корень
            }

            current = next;
        }

        throw new RuntimeException("Метод не сошелся за " + maxIterations + " итераций");
    }

    // Дополнительный метод для решения уравнения f(x) = 0 через x = g(x)
    public double findRoot() {
        return apply(0); // Используем метод apply для нахождения корня
    }
}
//Используется для решения уравнений вида: x = g(x) (где g(x) — переданная функция).