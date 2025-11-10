package concurrent;

import functions.UnitFunction;
import functions.TabulatedFunction;
import functions.LinkedListTabulatedFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class MultiplyingTaskExecutor {
    public static void main(String[] args) throws InterruptedException {
        TabulatedFunction function = new LinkedListTabulatedFunction(unitFunction, 1, 1000, 1000);
        UnitFunction unitFunction = new UnitFunction();
        List<Thread> threads = new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            MultiplyingTask task = new MultiplyingTask(function);
            Thread thread = new Thread(task, "Поток - " + (i + 1));
            threads.add(thread);
        }

        System.out.println("\nЗапущено " + threads.size() + " потоков.");


        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("\nРезультат " + function.toString());
    }
}
