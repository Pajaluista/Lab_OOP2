package concurrent;

import functions.TabulatedFunction;

public class WriteTask implements Runnable {
    private final TabulatedFunction function;
    private final double value;

    public WriteTask(TabulatedFunction function, double value) {
        this.function = function;
        this.value = value;
    }

    @Override
    public void run() {
        int count = function.getCount();
        for (int i = 0; i < count; i++) {
            synchronized (function) {//Это — блок синхронизации в Java.Он гарантирует, что только один поток одновременно может выполнять код внутри этого блока для одного и того же объекта-монитора
                function.setY(i, value);
                System.out.printf("Writing for index %d complete%n", i);
            }

        }
    }
}