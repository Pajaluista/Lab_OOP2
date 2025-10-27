package io;

import functions.TabulatedFunction;
import functions.factory.TabulatedFunctionFactory;
import functions.factory.ArrayTabulatedFunctionFactory;
import functions.factory.LinkedListTabulatedFunctionFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 Это демонстрационный класс, который:
 Считывает табулированную функцию из текстового файла.
 Создаёт две функции с разными реализациями:
 ArrayTabulatedFunction (на основе массива).
 LinkedListTabulatedFunction (на основе списка).
 */
public class TabulatedFunctionFileReader {

    public static void main(String[] args) {
        // Используем try-with-resources для двух потоков чтения из одного файла
        try (
                FileReader fileReader1 = new FileReader("input/function.txt");
                BufferedReader bufferedReader1 = new BufferedReader(fileReader1);

                FileReader fileReader2 = new FileReader("input/function.txt");
                BufferedReader bufferedReader2 = new BufferedReader(fileReader2)
        ) {
            // Создаем фабрики для разных типов функций
            TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
            TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();

            // Читаем функции с помощью соответствующих фабрик
            TabulatedFunction arrayFunction = FunctionsIO.readTabulatedFunction(bufferedReader1, arrayFactory);
            TabulatedFunction linkedListFunction = FunctionsIO.readTabulatedFunction(bufferedReader2, linkedListFactory);

            // Выводим функции в консоль
            System.out.println("ArrayTabulatedFunction:");
            System.out.println(arrayFunction.toString());

            System.out.println("\nLinkedListTabulatedFunction:");
            System.out.println(linkedListFunction.toString());

            // Проверяем, что функции эквивалентны
            System.out.println("\nФункции эквивалентны: " + arrayFunction.equals(linkedListFunction));

        } catch (IOException e) {
            // Обрабатываем исключение - выводим стектрейс в поток ошибок
            System.err.println("Ошибка при чтении файла:");
            e.printStackTrace();
        }
    }
}