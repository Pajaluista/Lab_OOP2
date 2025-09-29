package functions;

public class CompositeFunction implements MathFunction {
    private final MathFunction firstFunction;
    private final MathFunction secondFunction;

    public CompositeFunction(MathFunction firstFunction, MathFunction secondFunction) {
        this.firstFunction = firstFunction;
        this.secondFunction = secondFunction;
    }

    @Override
    public double apply(double x) {
        // Сначала применяем первую функцию, потом вторую к результату
        double firstResult = firstFunction.apply(x);
        return secondFunction.apply(firstResult);
    }
    // Дополнительные методы для удобства (опционально)
    public MathFunction getFirstFunction() {
        return firstFunction;
    }

    public MathFunction getSecondFunction() {
        return secondFunction;
    }
}