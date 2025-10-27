package functions;

//определяет как вставлять точки в табулированную функцию.
public interface Insertable {

   //вставляет или обновляет точку (x, y) в табулированной функции
    void insert(double x, double y);
}