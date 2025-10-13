package functions;

public class RungeKuttaSolver {
    public static double solve(DifferentialEquation equation, double x0, double y0, double xTarget, double stepSize) {

        if (Math.abs(xTarget - x0) < 1e-10) {//xTarget — точка, в которой нужно получить y
            return y0;
        }

        double x = x0;
        double y = y0;
        double h = Math.abs(stepSize);

        // Определяем направление интегрирования
        if (xTarget < x0) {
            h = -h;
        }

        // Интегрируем пока не достигнем целевой точки
        while (Math.abs(x - xTarget) > Math.abs(h) &&
                Math.abs(x - xTarget) > 1e-10) {

            // Чтобы не перескочить целевую точку
            if (Math.abs(x + h - xTarget) > Math.abs(x - xTarget)) {
                h = xTarget - x;
            }

            double k1 = h * equation.calculate(x, y);
            double k2 = h * equation.calculate(x + h/2, y + k1/2);
            double k3 = h * equation.calculate(x + h/2, y + k2/2);
            double k4 = h * equation.calculate(x + h, y + k3);

            y = y + (k1 + 2*k2 + 2*k3 + k4) / 6;
            x = x + h;
        }

        return y;
    }

//Использует итеративное уточнение с уменьшением шага, пока разница между результатами не станет меньше epsilon
    public static double solveWithAdaptiveStep(DifferentialEquation equation, double x0, double y0, double xTarget, double epsilon, int maxIterations) {

        double h = (xTarget - x0) / 10.0; // начальный шаг
        double result = y0;
        double previousResult;
        int iterations = 0;

        do {
            previousResult = result;
            result = solve(equation, x0, y0, xTarget, h);
            h /= 2; // уменьшаем шаг для повышения точности
            iterations++;
        } while (Math.abs(result - previousResult) > epsilon &&
                iterations < maxIterations);

        return result;
    }


    public static TabulatedFunction solveAsTabulatedFunction(DifferentialEquation equation, double x0, double y0, double xFrom, double xTo, int count) {

        if (count < 2) {
            throw new IllegalArgumentException("Count must be at least 2");
        }

        double[] xValues = new double[count];
        double[] yValues = new double[count];

        // Определяем шаг
        double step = (xTo - xFrom) / (count - 1);

        // Находим начальную точку в нашем интервале
        double currentX = xFrom;
        double currentY = solve(equation, x0, y0, xFrom, step);

        // Заполняем таблицу
        for (int i = 0; i < count; i++) {
            xValues[i] = currentX;
            yValues[i] = currentY;

            // Вычисляем следующее значение методом Рунге-Кутты
            if (i < count - 1) {
                double k1 = step * equation.calculate(currentX, currentY);
                double k2 = step * equation.calculate(currentX + step/2, currentY + k1/2);
                double k3 = step * equation.calculate(currentX + step/2, currentY + k2/2);
                double k4 = step * equation.calculate(currentX + step, currentY + k3);

                currentY = currentY + (k1 + 2*k2 + 2*k3 + k4) / 6;
                currentX += step;
            }
        }

        return new ArrayTabulatedFunction(xValues, yValues);
    }
}