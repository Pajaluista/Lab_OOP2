package operations;

import functions.MathFunction;
/**Что такое RightSteppingDifferentialOperator?
 📌 Это класс, который реализует правую разностную производную.
 Он наследуется от SteppingDifferentialOperator.
 Использует шаг step для приближённого вычисления производной.
 */
public class RightSteppingDifferentialOperator extends SteppingDifferentialOperator {
    public RightSteppingDifferentialOperator(double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return new MathFunction() {
            @Override
            public double apply(double x) {
                return (function.apply(x + step) - function.apply(x)) / step;
            }
        };
    }
}