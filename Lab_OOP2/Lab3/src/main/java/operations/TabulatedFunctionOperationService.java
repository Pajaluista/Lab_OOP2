package operations;

import exceptions.InconsistentFunctionsException;
import functions.TabulatedFunction;
import functions.Point;
import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;
import java.util.Iterator;

/**
 Это класс, который позволяет выполнять операции над табулированными функциями:
 Сложение, вычитание, умножение, деление.
 Использует фабрику для создания результата нужного типа.

 */
 public class TabulatedFunctionOperationService {
    private TabulatedFunctionFactory factory;

    public TabulatedFunctionOperationService() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public static Point[] asPoints(TabulatedFunction function) {
        if (function == null) {
            throw new IllegalArgumentException("Tabulated function cannot be null");
        }

        Point[] points = new Point[function.getCount()];
        int i = 0;
        for (Point point : function) {
            points[i++] = point;
        }
        return points;
    }

    private interface BiOperation {
        double apply(double u, double v);
    }

    private TabulatedFunction doOperation(TabulatedFunction a, TabulatedFunction b, BiOperation operation) {
        if (a.getCount() != b.getCount()) {
            throw new InconsistentFunctionsException("Functions have different number of points");
        }

        Point[] pointsA = asPoints(a);
        Point[] pointsB = asPoints(b);

        double[] xValues = new double[a.getCount()];
        double[] yValues = new double[a.getCount()];

        for (int i = 0; i < a.getCount(); i++) {
            if (Math.abs(pointsA[i].x - pointsB[i].x) > 1e-10) {
                throw new InconsistentFunctionsException("x values are inconsistent");
            }
            xValues[i] = pointsA[i].x;
            yValues[i] = operation.apply(pointsA[i].y, pointsB[i].y);
        }

        return factory.create(xValues, yValues);
    }

    public TabulatedFunction add(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u + v);
    }

    public TabulatedFunction subtract(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u - v);
    }


    public TabulatedFunction multiply(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u * v);
    }


    public TabulatedFunction divide(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> {
            if (Math.abs(v) < 1e-10) {
                throw new ArithmeticException("Division by zero at point with y = " + v);
            }
            return u / v;
        });
    }
}