package concurrent;

import functions.UnitFunction;
import functions.TabulatedFunction;
import functions.LinkedListTabulatedFunction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ImprovedMultiplyingTaskExecutor {

    static class TrackableMultiplyingTask implements Runnable {
        private final TabulatedFunction function;
        private final String taskId;
        private volatile boolean completed = false;

        public TrackableMultiplyingTask(TabulatedFunction function, String taskId) {
            this.function = function;
            this.taskId = taskId;
        }

        public boolean isCompleted() {
            return completed;
        }

        public String getTaskId() {
            return taskId;
        }

        @Override
        public void run() {
            for (int i = 0; i < function.getCount(); i++) {
                synchronized (function) {
                    double currentY = function.getY(i);
                    function.setY(i, currentY * 2);
                }
            }
            completed = true;
            System.out.println("Задача " + taskId + " завершена потоком " + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        TabulatedFunction function = new LinkedListTabulatedFunction(new UnitFunction(), 1, 1000, 1000);
        int taskCount = 10;

        // Используем ConcurrentHashMap для потокобезопасного отслеживания задач
        Map<String, TrackableMultiplyingTask> tasks = new ConcurrentHashMap<>();

        // Создаем и запускаем задачи
        for (int i =  ɪ; i < taskCount; i++) {
            String taskId = "Task-" + (i + 1);
            TrackableMultiplyingTask task = new TrackableMultiplyingTask(function, taskId);
            tasks.put(taskId, task);
            new Thread(task, "Worker-" + (i + 1)).start();
        }

        // Ожидаем завершения всех задач
        while (!tasks.isEmpty()) {
            Iterator<Map.Entry<String, TrackableMultiplyingTask>> iterator = tasks.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, TrackableMultiplyingTask> entry = iterator.next();
                if (entry.getValue().isCompleted()) {
                    System.out.println("Удаляем завершенную задачу: " + entry.getKey());
                    iterator.remove();
                }
            }

            // Небольшая пауза
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Все задачи завершены!");
        System.out.println("Финальный результат:");
        for (int i = 0; i < Math.min(10, function.getCount()); i++) {
            System.out.printf("x = %f, y = %f%n", function.getX(i), function.getY(i));
        }

        // Проверяем корректность результата (должно быть 2^10 = 1024)
        double expected = Math.pow(2, taskCount);
        System.out.printf("Ожидаемое значение: %f%n", expected);
        System.out.printf("Фактическое значение: %f%n", function.getY(0));
    }
}