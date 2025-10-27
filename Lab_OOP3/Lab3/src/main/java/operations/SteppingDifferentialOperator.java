package operations;

import functions.MathFunction;

/**
 Это абстрактный класс, который является общим для всех операторов с шагом.
 Он реализует DifferentialOperator<MathFunction>.
 Сохраняет шаг step, который используется в разностных производных.
 Проверяет, что шаг выполнен правильно.
 */
public abstract class SteppingDifferentialOperator implements DifferentialOperator<MathFunction> {
    protected double step;

    public SteppingDifferentialOperator(double step) {
        if (step <= 0 || Double.isInfinite(step) || Double.isNaN(step)) {
            throw new IllegalArgumentException("Invalid step value: " + step);
        }
        this.step = step;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        if (step <= 0 || Double.isInfinite(step) || Double.isNaN(step)) {
            throw new IllegalArgumentException("Invalid step value: " + step);
        }
        this.step = step;
    }
}