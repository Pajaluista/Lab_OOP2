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
        if (index >= 0) {
            // Точное совпадение
            return getY(index);
        }

        // Интерполяция
        int floorIndex = floorIndexOfX(x);
        return interpolate(x, floorIndex);
    }

    // Вспомогательный метод для линейной интерполяции
    protected double interpolate(double x, double leftX, double leftY, double rightX, double rightY) {
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
}