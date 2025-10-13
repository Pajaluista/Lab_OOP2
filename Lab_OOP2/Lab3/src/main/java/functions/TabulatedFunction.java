package functions;

public interface TabulatedFunction extends MathFunction, Iterable<Point> { //Это интерфейс, который определяет, как работать с таблицей значений функции.


     //Возвращает количество табулированных значений

    int getCount();

    //Возвращает значение аргумента x по указанному индексу

    double getX(int index);

    //Возвращает значение функции y по указанному индексу

    double getY(int index);

    // Устанавливает значение функции y по указанному индексу


    void setY(int index, double value);

    //Возвращает индекс аргумента x в таблице
    int indexOfX(double x);

    //Возвращает индекс первого вхождения значения y в таблице

    int indexOfY(double y);

    //Возвращает самый левый (минимальный) x в области определения

    double leftBound();

    //Возвращает самый правый (максимальный) x в области определения

    double rightBound();
}