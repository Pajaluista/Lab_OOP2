package functions;

public class IdentityFunction implements MathFunction { // Добавляем 'implements MathFunction'
    @Override //@Override - это аннотация в Java. Она ставится перед методом и говорит компилятору: "Эй, этот метод не новый - он переопределяет метод из родительского класса или интерфейса!"
    public double apply(double x) { // Реализуем метод apply
        return x;
    }
}
