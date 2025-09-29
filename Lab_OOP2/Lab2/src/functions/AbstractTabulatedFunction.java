package functions;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {

    // Абстрактные методы, которые должны быть реализованы в подклассах
    public abstract int getCount();
    public abstract double getX(int index);
    public abstract double getY(int index);
    public abstract void setY(int index, double value);
    public abstract double leftBound();
    public abstract double rightBound();
    public abstract int indexOfX(double x);
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

    // Вспомогательный метод для линейной интерполяции (ИСПРАВЛЕННЫЙ ПОРЯДОК АРГУМЕНТОВ)
    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        if (Math.abs(rightX - leftX) < 1e-10) {
            return leftY; // Если точки совпадают
        }
        return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
    }

    // Метод для получения строкового представления
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TabulatedFunction[");
        for (int i = 0; i < getCount(); i++) {
            if (i > 0) sb.append(", ");
            sb.append("(").append(getX(i)).append("; ").append(getY(i)).append(")");
        }
        sb.append("]");
        return sb.toString();
    }

    // Можно добавить метод equals для сравнения табличных функций
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        TabulatedFunction other = (TabulatedFunction) obj;
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
    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < getCount(); i++) {
            long xBits = Double.doubleToLongBits(getX(i));
            long yBits = Double.doubleToLongBits(getY(i));
            result = 31 * result + (int) (xBits ^ (xBits >>> 32));
            result = 31 * result + (int) (yBits ^ (yBits >>> 32));
        }
        return result;
    }
}