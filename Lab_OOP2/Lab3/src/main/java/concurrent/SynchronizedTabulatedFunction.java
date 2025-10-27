package concurrent;

import functions.TabulatedFunction;
import functions.Point;

public class SynchronizedTabulatedFunction implements TabulatedFunction {
    private final TabulatedFunction function;
    private final Object lock = new Object();

    public SynchronizedTabulatedFunction(TabulatedFunction function) {
        this.function = function;
    }

    @Override
    public int getCount() {
        synchronized (lock) {
            return function.getCount();
        }
    }

    @Override
    public double getX(int index) {
        synchronized (lock) {
            return function.getX(index);
        }
    }

    @Override
    public double getY(int index) {
        synchronized (lock) {
            return function.getY(index);
        }
    }

    @Override
    public void setY(int index, double value) {
        synchronized (lock) {
            function.setY(index, value);
        }
    }

    @Override
    public int indexOfX(double x) {
        synchronized (lock) {
            return function.indexOfX(x);
        }
    }

    @Override
    public int indexOfY(double y) {
        synchronized (lock) {
            return function.indexOfY(y);
        }
    }

    @Override
    public double leftBound() {
        synchronized (lock) {
            return function.leftBound();
        }
    }

    @Override
    public double rightBound() {
        synchronized (lock) {
            return function.rightBound();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        synchronized (lock) {
            Point[] points = new Point[function.getCount()];
            for (int i = 0; i < function.getCount(); i++) {
                points[i] = new Point(function.getX(i), function.getY(i));
            }
            return Arrays.asList(points).iterator();
        }
    }

    // Интерфейс для операций
    public interface Operation<T> {
        T apply(SynchronizedTabulatedFunction function);
    }

    // Метод для выполнения операций в синхронизированном блоке
    public <T> T doSynchronously(Operation<? extends T> operation) {
        synchronized (lock) {
            return operation.apply(this);
        }
    }
}