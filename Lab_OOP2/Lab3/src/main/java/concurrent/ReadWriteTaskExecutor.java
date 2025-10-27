package concurrent;

import functions.*;

public class ReadWriteTaskExecutor {
    public static void main(String[] args) {

        MathFunction constantFunc = new ConstantFunction(-1.0);


        TabulatedFunction tabFunc = new LinkedListTabulatedFunction(
                constantFunc,   // исходная функция
                1.0,           // xFrom
                1000.0,        // xTo
                1000           // count
        );


        ReadTask readTask = new ReadTask(tabFunc);
        WriteTask writeTask = new WriteTask(tabFunc, 0.5);

        Thread reader = new Thread(readTask, "Reader");
        Thread writer = new Thread(writeTask, "Writer");

        reader.start();
        writer.start();

        try {
            reader.join();
            writer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}