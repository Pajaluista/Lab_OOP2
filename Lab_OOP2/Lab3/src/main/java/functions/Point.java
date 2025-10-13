package functions;


//  Класс для представления точки (x, y) в табулированной функции
//  Используется для предоставления значений независимо от способа хранения

public class Point {

    public final double x;


    public final double y;

//Создаёт точку с заданными x и y
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}