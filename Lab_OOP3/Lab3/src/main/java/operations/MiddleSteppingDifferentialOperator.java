package operations;

import functions.MathFunction;

/**
 Это класс, который реализует среднюю разностную производную.
 Он наследуется от SteppingDifferentialOperator.
 Использует шаг step для приближённого вычисления производной.
 */
public class MiddleSteppingDifferentialOperator extends SteppingDifferentialOperator {
    public MiddleSteppingDifferentialOperator(double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return new MathFunction() {
            @Override
            public double apply(double x) {
                // Средняя разностная производная: (f(x + step) - f(x - step)) / (2 * step)
                return (function.apply(x + step) - function.apply(x - step)) / (2 * step);
            }
        };
    }
}