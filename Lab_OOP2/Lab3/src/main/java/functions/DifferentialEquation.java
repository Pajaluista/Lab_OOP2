package functions;


//Интерфейс для представления правой части дифференциального уравнения
 //dy/dx = f(x, y)

@FunctionalInterface
public interface DifferentialEquation {

//уравнение вида: dy/dx = f(x, y)
//x — независимая переменная.
//y — зависимая переменная.
//f(x, y) — правая часть которую нужно вычислить.
    double calculate(double x, double y);
}