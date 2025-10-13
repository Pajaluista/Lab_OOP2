package functions;

public interface MathFunction {
    double apply(double x);
    default CompositeFunction andThen(MathFunction afterFunction) {
        return new CompositeFunction(this, afterFunction);
    }
}
//Класс, который реализует меня, должен уметь вычислять значение функции в точке x
//f.andThen(g).andThen(h); // result(x) = h(g(f(x))) = h(g(x^2)) = h(x^2)