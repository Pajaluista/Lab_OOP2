package concurrent;

import functions.TabulatedFunction;

public class ReadTask implements Runnable {// класс не синхронизирован . Если TabulatedFunction будет изменяться другим потоком во время чтения (например, через setY()), возможны неконсистентные данные или исключения.
    private final TabulatedFunction function;

    public ReadTask(TabulatedFunction function) {
        this.function = function;
    }

    @Override
    public void run() {
        int count = function.getCount();
        for (int i = 0; i < count; i++) {
            double x = function.getX(i);
            double y = function.getY(i);
            System.out.printf("After read: i = %d, x = %f, y = %f%n", i, x, y);
        }
    }
}//Thread thread = new Thread(new ReadTask(func)); запустить код в отдельном потоке выполнения в Java