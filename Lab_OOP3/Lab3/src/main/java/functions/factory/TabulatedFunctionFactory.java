package functions.factory;

import functions.TabulatedFunction;

/**
 * Фабрика для создания табулированных функций
 * Паттерн "Абстрактная Фабрика"
 */
public interface TabulatedFunctionFactory {

    /**
     * Создает табулированную функцию на основе массивов x и y значений
     *
     * @param xValues массив значений аргумента
     * @param yValues массив значений функции
     * @return новая табулированная функция
     */
    TabulatedFunction create(double[] xValues, double[] yValues);

    default TabulatedFunction createUnmodifiable(double[] xValues, double[] yValues) {
        return new functions.UnmodifiableTabulatedFunction(create(xValues, yValues));
    }

    default TabulatedFunction createStrictUnmodifiable(double[] xValues, double[] yValues) {
        return new functions.UnmodifiableTabulatedFunction(
                new functions.StrictTabulatedFunction(create(xValues, yValues)));
    }

}