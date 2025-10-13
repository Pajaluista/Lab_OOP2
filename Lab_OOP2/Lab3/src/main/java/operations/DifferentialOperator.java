package operations;

import functions.MathFunction;

/**
 Это операция, которая берёт функцию и возвращает её производную.
 В математике: D(f) = f'(x) — производная функции f(x).
 В программировании: метод, который принимает функцию и возвращает другую функцию — производную.
 */
public interface DifferentialOperator<T extends MathFunction> {

    T derive(T function);
}