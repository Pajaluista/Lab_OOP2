package concurrent;

import functions.TabulatedFunction;
import functions.Point;
import java.util.Iterator;
import java.util.Arrays;

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

    // ДОБАВЛЕН недостающий метод apply(double)
    @Override
    public double apply(double x) {
        synchronized (lock) {
            return function.apply(x);
        }
    }

    @Override
    public Iterator<Point> iterator() {
        Point[] snapshot;
        synchronized (lock) {
            // Создаём копию всех точек — "снимок" состояния функции
            int count = function.getCount();
            snapshot = new Point[count];
            for (int i = 0; i < count; i++) {
                snapshot[i] = new Point(function.getX(i), function.getY(i));
            }
        }

        // Возвращаем анонимный итератор по копии (snapshot)
        return new Iterator<Point>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < snapshot.length;
            }

            @Override
            public Point next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return snapshot[index++];
            }
        };
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