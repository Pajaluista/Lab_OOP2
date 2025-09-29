package functions;

import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction  implements Insertable, Removable  {
    private double[] xValues;
    private double[] yValues;
    private int count;

    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("Arrays must have same length");
        }
        if (xValues.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        this.count = xValues.length;
        this.xValues = Arrays.copyOf(xValues, count);
        this.yValues = Arrays.copyOf(yValues, count);

        // Проверка упорядоченности
        for (int i = 1; i < count; i++) {
            if (this.xValues[i] <= this.xValues[i - 1]) {
                throw new IllegalArgumentException("xValues must be strictly increasing");
            }
        }
    }

    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        this.count = count;
        this.xValues = new double[count];
        this.yValues = new double[count];

        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        if (Math.abs(xFrom - xTo) < 1e-10) {
            // Все точки одинаковые
            double value = source.apply(xFrom);
            Arrays.fill(xValues, xFrom);
            Arrays.fill(yValues, value);
        } else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; i++) {
                xValues[i] = xFrom + i * step;
                yValues[i] = source.apply(xValues[i]);
            }
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getX(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return xValues[index];
    }

    @Override
    public double getY(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return yValues[index];
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        yValues[index] = value;
    }

    @Override
    public int indexOfX(double x) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(xValues[i] - x) < 1e-10) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(yValues[i] - y) < 1e-10) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[count - 1];
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (x < xValues[0]) {
            return 0;
        }
        if (x > xValues[count - 1]) {
            return count - 1;
        }

        for (int i = 1; i < count; i++) {
            if (x < xValues[i]) {
                return i - 1;
            }
        }
        return count - 1;
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (count == 1) {
            return yValues[0];
        }
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (count == 1) {
            return yValues[0];
        }
        return interpolate(x, xValues[count - 2], xValues[count - 1], yValues[count - 2], yValues[count - 1]);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (count == 1) {
            return yValues[0];
        }

        if (floorIndex == 0) {
            return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
        } else if (floorIndex == count - 1) {
            return interpolate(x, xValues[count - 2], xValues[count - 1], yValues[count - 2], yValues[count - 1]);
        } else {
            return interpolate(x, xValues[floorIndex], xValues[floorIndex + 1],
                    yValues[floorIndex], yValues[floorIndex + 1]);
        }
    }
    @Override
    public void remove(int index) {
        if (index < 0 || index >= count) { // Лучше использовать count вместо getCount()
            throw new IllegalArgumentException("Invalid index: " + index);
        }

        if (count <= 2) {
            throw new IllegalStateException("Cannot remove point: minimum 2 points required");
        }

        // Создаем новые массивы на 1 элемент меньше
        double[] newXValues = new double[count - 1];
        double[] newYValues = new double[count - 1];

        // Копируем элементы до удаляемого индекса
        System.arraycopy(xValues, 0, newXValues, 0, index);
        System.arraycopy(yValues, 0, newYValues, 0, index);

        // Копируем элементы после удаляемого индекса
        System.arraycopy(xValues, index + 1, newXValues, index, count - index - 1);
        System.arraycopy(yValues, index + 1, newYValues, index, count - index - 1);

        // Заменяем старые массивы новыми
        xValues = newXValues;
        yValues = newYValues;
        count--; // ← ВАЖНО: уменьшаем количество точек
    }
    @Override
    public void insert(double x, double y) {
        // Проверяем, есть ли уже такой x
        int existingIndex = indexOfX(x);
        if (existingIndex != -1) {
            // Заменяем существующее значение y
            setY(existingIndex, y);
            return;
        }

        // Находим позицию для вставки
        int insertIndex = 0;
        while (insertIndex < count && xValues[insertIndex] < x) {
            insertIndex++;
        }

        // Создаем новые массивы на 1 элемент больше
        double[] newXValues = new double[count + 1];
        double[] newYValues = new double[count + 1];

        // Копируем элементы до позиции вставки
        System.arraycopy(xValues, 0, newXValues, 0, insertIndex);
        System.arraycopy(yValues, 0, newYValues, 0, insertIndex);

        // Вставляем новый элемент
        newXValues[insertIndex] = x;
        newYValues[insertIndex] = y;

        // Копируем элементы после позиции вставки
        System.arraycopy(xValues, insertIndex, newXValues, insertIndex + 1, count - insertIndex);
        System.arraycopy(yValues, insertIndex, newYValues, insertIndex + 1, count - insertIndex);

        // Заменяем старые массивы новыми
        xValues = newXValues;
        yValues = newYValues;
        count++;
    }
}
