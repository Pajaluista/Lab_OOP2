package io;

import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 Это демонстрационный класс, который:
 Создаёт две табулированные функции:
 На основе массива (ArrayTabulatedFunction).
 На основе списка (LinkedListTabulatedFunction
 */
public class TabulatedFunctionFileOutputStream {

    public static void main(String[] args) {
        // Создаем две тестовые функции
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0, 25.0}; // x²

        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(xValues, yValues);

        // Используем try-with-resources для двух потоков
        try (
                FileOutputStream arrayFileStream = new FileOutputStream("output/array function.bin");
                BufferedOutputStream arrayBufferedStream = new BufferedOutputStream(arrayFileStream);

                FileOutputStream linkedListFileStream = new FileOutputStream("output/linked list function.bin");
                BufferedOutputStream linkedListBufferedStream = new BufferedOutputStream(linkedListFileStream)
        ) {
            // Записываем функции в соответствующие бинарные файлы
            FunctionsIO.writeTabulatedFunction(arrayBufferedStream, arrayFunction);
            FunctionsIO.writeTabulatedFunction(linkedListBufferedStream, linkedListFunction);

            System.out.println("Функции успешно записаны в бинарные файлы:");
            System.out.println("- output/array function.bin");
            System.out.println("- output/linked list function.bin");
            System.out.println("Размер файлов: " + (5 * 2 * 8 + 4) + " байт каждый"); // 5 точек * 2 значения * 8 байт + 4 байта на count

        } catch (IOException e) {
            // Обрабатываем исключение - выводим стектрейс в поток ошибок
            System.err.println("Ошибка при записи бинарных файлов:");
            e.printStackTrace();
        }
    }
}