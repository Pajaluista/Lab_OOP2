package operations;

import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.TabulatedFunction;
import functions.factory.LinkedListTabulatedFunctionFactory;
import functions.Point;
import java.util.Iterator;

/**
 Это абстрактный класс, который является общим для всех операторов с шагом.
 Он реализует DifferentialOperator<MathFunction>.
 Сохраняет шаг step, который используется в разностных производных.
 Проверяет, что шаг выполнен правильно.
 */
public class TabulatedDifferentialOperator implements DifferentialOperator<TabulatedFunction> {

    private TabulatedFunctionFactory factory;


    public TabulatedDifferentialOperator(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }


    public TabulatedDifferentialOperator() {
        this(new ArrayTabulatedFunctionFactory());
    }

    // Геттер для фабрики
    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    // Сеттер для фабрики
    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    @Override
    public TabulatedFunction derive(TabulatedFunction function) {
        int count = function.getCount();

        // Создаем массивы для новой функции
        double[] xValues = new double[count];
        double[] derivativeValues = new double[count];

        // Копируем x значения (они остаются теми же)
        for (int i = 0; i < count; i++) {
            xValues[i] = function.getX(i);
        }

        // Вычисляем производные для первых n-1 точек (правая разностная производная)
        for (int i = 0; i < count - 1; i++) {
            double deltaX = function.getX(i + 1) - function.getX(i);
            double deltaY = function.getY(i + 1) - function.getY(i);
            derivativeValues[i] = deltaY / deltaX;
        }

        // Для последней точки используем левую разностную производную
        double lastDeltaX = function.getX(count - 1) - function.getX(count - 2);
        double lastDeltaY = function.getY(count - 1) - function.getY(count - 2);
        derivativeValues[count - 1] = lastDeltaY / lastDeltaX;

        // Создаем новую табулированную функцию через фабрику
        return factory.create(xValues, derivativeValues);
    }
}