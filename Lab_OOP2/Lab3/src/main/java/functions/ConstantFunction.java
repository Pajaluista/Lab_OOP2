package functions;

public class ConstantFunction implements MathFunction {
    private final double constant;

    public ConstantFunction(double constant) {
        if (Double.isNaN(constant)) {
            throw new IllegalArgumentException("Константа не может быть NaN");
        }

        this.constant = constant;
    }

    @Override
    public double apply(double x) {
        return constant;
    }

    public double getConstant() {
        return constant;
    }

    @Override
    //Сравнивает два объекта ConstantFunction и проверяет, равны ли они.
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;//Если obj — другой класс, возвращаем false.

        ConstantFunction that = (ConstantFunction) obj;//Приводим obj к типу ConstantFunction, чтобы получить доступ к полю constant
        return Double.compare(that.constant, constant) == 0;//сравниваем 2 числа дабл
    }

    @Override
    //Возвращает хеш-код объекта.
    public int hashCode() {
        return Double.hashCode(constant);
    }

    @Override
    public String toString() {
        return "ConstantFunction{" +
                "constant=" + constant +
                '}';
    }
}
//Реализует постоянную функцию, то есть:
//f(x) = C, где C — константа.
//Неважно, какой x передан, результат всегда один и тот же.