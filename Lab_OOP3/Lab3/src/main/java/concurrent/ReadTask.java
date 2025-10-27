package concurrent;

import functions.TabulatedFunction;

public class ReadTask implements Runnable {
    private final TabulatedFunction function;

    public ReadTask(TabulatedFunction function) {
        this.function = function;
    }

    @Override
    public void run() {
        int count = function.getCount();
        for (int i = 0; i < count; i++) {
            // Синхронизация НА КАЖДОЙ ИТЕРАЦИИ
            synchronized (function) {
                double x = function.getX(i);
                double y = function.getY(i);
                System.out.printf("After read: i = %d, x = %f, y = %f%n", i, x, y);
            }
            // После выхода из блока — монитор отпущен, другой поток может работать
        }
    }
}//Thread thread = new Thread(new ReadTask(func)); запустить код в отдельном потоке выполнения в Java