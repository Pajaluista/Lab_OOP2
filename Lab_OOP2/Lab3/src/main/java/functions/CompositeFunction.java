package functions;

public class CompositeFunction implements MathFunction {//Реализует композицию двух функций.
    private final MathFunction firstFunction;
    private final MathFunction secondFunction;

    public CompositeFunction(MathFunction firstFunction, MathFunction secondFunction) {
        if (firstFunction == null || secondFunction == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        this.firstFunction = firstFunction;
        this.secondFunction = secondFunction;
    }

    @Override
    public double apply(double x) {
        return secondFunction.apply(firstFunction.apply(x));
    }

    // Дополнительные методы для удобства (опционально)
    public MathFunction getFirstFunction() {
        return firstFunction;
    }

    public MathFunction getSecondFunction() {
        return secondFunction;
    }
}
//firstFunction — первая функция, которую применяют к x.
//secondFunction — вторая функция, которая применяется к результату первой.