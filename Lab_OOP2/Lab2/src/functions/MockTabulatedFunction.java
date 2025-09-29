package functions;

public class MockTabulatedFunction extends AbstractTabulatedFunction {
    private final double x0;
    private final double x1;
    private final double y0;
    private final double y1;

    public MockTabulatedFunction(double x0, double x1, double y0, double y1) {
        if (x0 >= x1) {
            throw new IllegalArgumentException("x0 must be less than x1");
        }
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
    }

    // Реализация абстрактных методов из TabulatedFunction
    @Override
    public int getCount() {
        return 2; // Всегда 2 точки
    }

    @Override
    public double getX(int index) {
        switch (index) {
            case 0: return x0;
            case 1: return x1;
            default: throw new IllegalArgumentException("Index must be 0 or 1");
        }
    }

    @Override
    public double getY(int index) {
        switch (index) {
            case 0: return y0;
            case 1: return y1;
            default: throw new IllegalArgumentException("Index must be 0 or 1");
        }
    }

    @Override
    public void setY(int index, double value) {
        throw new UnsupportedOperationException("Mock object is immutable");
    }

    @Override
    public double leftBound() {
        return x0;
    }

    @Override
    public double rightBound() {
        return x1;
    }

    @Override
    public int indexOfX(double x) {
        if (Math.abs(x - x0) < 1e-10) return 0;
        if (Math.abs(x - x1) < 1e-10) return 1;
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        if (Math.abs(y - y0) < 1e-10) return 0;
        if (Math.abs(y - y1) < 1e-10) return 1;
        return -1;
    }

    // Реализация абстрактных методов для интерполяции/экстраполяции
    @Override
    protected int floorIndexOfX(double x) {
        if (x < x0) return 0; // Экстраполяция слева
        if (x < x1) return 0; // Интерполяция в единственном интервале
        return 0; // Экстраполяция справа
    }

    @Override
    protected double extrapolateLeft(double x) {
        // Используем первый интервал для экстраполяции слева
        return interpolate(x, x0, y0, x1, y1);
    }

    @Override
    protected double extrapolateRight(double x) {
        // Используем первый интервал для экстраполяции справа
        return interpolate(x, x0, y0, x1, y1);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        // floorIndex всегда 0, так как у нас только один интервал
        return interpolate(x, x0, y0, x1, y1);
    }
}