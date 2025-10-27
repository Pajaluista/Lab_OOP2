package functions;

public class IdentityFunction implements MathFunction {
    @Override //@Override - это аннотация в Java. Она ставится перед методом и говорит компилятору:
    public double apply(double x) { // Реализуем метод apply
        return x;
    }
}
