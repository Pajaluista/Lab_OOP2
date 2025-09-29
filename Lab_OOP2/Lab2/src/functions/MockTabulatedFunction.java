package functions;

public class MockTabulatedFunction extends AbstractTabulatedFunction {
    private final double x0, x1, y0, y1;

    public MockTabulatedFunction(double x0, double x1, double y0, double y1) {
        if (x0 >= x1) {
            throw new IllegalArgumentException("x0 must be less than x1");
        }
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public double getX(int index) {
        switch (index) {
            case 0: return x0;
            case 1: return x1;
            default: throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    @Override
    public double getY(int index) {
        switch (index) {
            case 0: return y0;
            case 1: return y1;
            default: throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    @Override
    public void setY(int index, double value) {
        throw new UnsupportedOperationException("Mock object is immutable");
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

    @Override
    public double leftBound() {
        return x0;
    }

    @Override
    public double rightBound() {
        return x1;
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (x < x0) return 0;
        if (x > x1) return 1; // Исправлено: должно возвращать 1 для экстраполяции справа
        return 0; // Для интерполяции в единственном интервале
    }

    @Override
    protected double extrapolateLeft(double x) {
        return interpolate(x, x0, x1, y0, y1);
    }

    @Override
    protected double extrapolateRight(double x) {
        return interpolate(x, x0, x1, y0, y1);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        return interpolate(x, x0, x1, y0, y1);
    }
}