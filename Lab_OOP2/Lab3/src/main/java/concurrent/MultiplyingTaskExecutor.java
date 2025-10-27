package concurrent;

import functions.UnitFunction;
import functions.TabulatedFunction;
import functions.LinkedListTabulatedFunction;

import java.util.ArrayList;
import java.util.List;

public class MultiplyingTaskExecutor {
    public static void main(String[] args) {
        // Создаем табулированную функцию
        TabulatedFunction function = new LinkedListTabulatedFunction(new UnitFunction(), 1, 1000, 1000);

        // Создаем список потоков
        List<Thread> threads = new ArrayList<>();
        int threadCount = 10;

        // Создаем и добавляем потоки
        for (int i = 0; i < threadCount; i++) {
            MultiplyingTask task = new MultiplyingTask(function);
            Thread thread = new Thread(task, "MultiplierThread-" + (i + 1));
            threads.add(thread);
        }

        // Запускаем все потоки
        for (Thread thread : threads) {
            thread.start();
        }

        // Даем время на выполнение
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Выводим результат
        System.out.println("Результат после выполнения всех потоков:");
        for (int i = 0; i < Math.min(10, function.getCount()); i++) {
            System.out.printf("x = %f, y = %f%n", function.getX(i), function.getY(i));
        }
    }
}