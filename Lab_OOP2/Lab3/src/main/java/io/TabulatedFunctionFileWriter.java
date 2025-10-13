package io;

import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 Это демонстрационный класс, который:
 Создаёт две табулированные функции:
 На основе массива (ArrayTabulatedFunction).
 На основе списка (LinkedListTabulatedFunction).
 */
public class TabulatedFunctionFileWriter {

    public static void main(String[] args) {
        // Создаем две тестовые функции
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0, 25.0}; // x²

        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(xValues, yValues);

        // Используем try-with-resources для двух потоков
        try (
                FileWriter arrayFileWriter = new FileWriter("output/array function.txt");
                BufferedWriter arrayBufferedWriter = new BufferedWriter(arrayFileWriter);

                FileWriter linkedListFileWriter = new FileWriter("output/linked list function.txt");
                BufferedWriter linkedListBufferedWriter = new BufferedWriter(linkedListFileWriter)
        ) {
            // Записываем функции в соответствующие файлы
            FunctionsIO.writeTabulatedFunction(arrayBufferedWriter, arrayFunction);
            FunctionsIO.writeTabulatedFunction(linkedListBufferedWriter, linkedListFunction);

            System.out.println("Функции успешно записаны в файлы:");
            System.out.println("- output/array function.txt");
            System.out.println("- output/linked list function.txt");

        } catch (IOException e) {
            // Обрабатываем исключение - выводим стектрейс в поток ошибок
            System.err.println("Ошибка при записи файлов:");
            e.printStackTrace();
        }
    }
}