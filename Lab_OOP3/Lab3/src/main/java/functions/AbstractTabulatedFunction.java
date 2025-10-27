package functions;

import exceptions.ArrayIsNotSortedException;
import exceptions.DifferentLengthOfArraysException;
import exceptions.InterpolationException;

import java.util.Iterator;
import java.util.NoSuchElementException;
public abstract class AbstractTabulatedFunction implements TabulatedFunction {

    // Абстрактные методы, которые должны быть реализованы в подклассах
    public abstract int getCount();
    public abstract double getX(int index);
    public abstract double getY(int index);
    public abstract void setY(int index, double value);
    public abstract double leftBound();
    public abstract double rightBound();
    public abstract int indexOfX(double x);//Ищет индекс точки, у которой x совпадает с заданным.
    public abstract int indexOfY(double y);

    // Защищенные методы для интерполяции/экстраполяции
    protected abstract int floorIndexOfX(double x);
    protected abstract double extrapolateLeft(double x);
    protected abstract double extrapolateRight(double x);
    protected abstract double interpolate(double x, int floorIndex);

    // Реализация метода apply из MathFunction
    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        }

        if (x > rightBound()) {
            return extrapolateRight(x);
        }

        int index = indexOfX(x);
        if (index != -1) {
            // Точное совпадение
            return getY(index);
        }

        // Интерполяция
        int floorIndex = floorIndexOfX(x);
        return interpolate(x, floorIndex);
    }

    // Вспомогательный метод для линейной интерполяции
    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        // ПРОВЕРКА ДЛЯ ИНТЕРПОЛЯЦИИ
        if (x < leftX || x > rightX) {
            throw new InterpolationException(
                    "x = " + x + " is outside interpolation interval [" + leftX + ", " + rightX + "]"
            );
        }

        if (Math.abs(rightX - leftX) < 1e-10) {
            return leftY;
        }
        return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
    }

    //Выводит строковое представление функции в формате:
    //ClassName count
    //[x1; y1]
    //[x2; y2]
    //...
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Первая строка: название класса и размер count
        sb.append(getClass().getSimpleName())
                .append(" ")
                .append(getCount())
                .append("\n");

        // Следующие строки: точки в квадратных скобках
        for (int i = 0; i < getCount(); i++) {
            sb.append("[")
                    .append(getX(i))
                    .append("; ")
                    .append(getY(i))
                    .append("]")
                    .append("\n");
        }

        return sb.toString();
    }

    // Можно добавить метод equals для сравнения табличных функций сравнивает два объекта и проверяет, равны ли они.
//    По умолчанию equals сравниваются ссылки (то есть это один и тот же объект в памяти?).
//    Но часто нужно сравнивать содержимое объектов, а не ссылки — **для этого переопределяют equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;//Сравнивает ссылки: если this и obj — один и тот же объект, то они равны.
        if (obj == null || getClass() != obj.getClass()) return false;

        TabulatedFunction other = (TabulatedFunction) obj;//Приводит obj к типу TabulatedFunction, чтобы получить доступ к методам getCount, getX, getY
        if (this.getCount() != other.getCount()) return false;

        for (int i = 0; i < getCount(); i++) {
            if (Math.abs(this.getX(i) - other.getX(i)) > 1e-10 ||
                    Math.abs(this.getY(i) - other.getY(i)) > 1e-10) {
                return false;
            }
        }
        return true;
    }

    // И hashCode для корректной работы с коллекциями
//    Это метод, который возвращает число — хеш-код объекта
//    Используется для быстрого поиска объектов в коллекциях, таких как: HashMap, HashSet, Hashtable
    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < getCount(); i++) {
            long xBits = Double.doubleToLongBits(getX(i));//Double.doubleToLongBits(...) — преобразует double в long
            long yBits = Double.doubleToLongBits(getY(i));
            result = 31 * result + (int) (xBits ^ (xBits >>> 32));
            result = 31 * result + (int) (yBits ^ (yBits >>> 32));
            //xBits >>> 32 — сдвиг вправо на 32 бита (берём старшие 32 бита).
            //xBits ^ (xBits >>> 32) — XOR старших и младших битов (получаем 32-битное число).
            //31 * result + ... — умножаем на 31, чтобы хеши были более уникальными (31 — хорошее число для хеширования).
        }
        return result;
    }
    public static void checkLengthIsTheSame(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new DifferentLengthOfArraysException(
                    "Arrays have different lengths: xValues.length = " + xValues.length +
                            ", yValues.length = " + yValues.length
            );
        }
    }
    //Проверяет, отсортированы ли значения x по возрастанию.
    public static void checkSorted(double[] xValues) {
        for (int i = 1; i < xValues.length; i++) {
            if (xValues[i] <= xValues[i - 1]) {
                throw new ArrayIsNotSortedException(
                        "Array is not sorted at index " + i +
                                ": " + xValues[i - 1] + " >= " + xValues[i]
                );
            }
        }
    }
}