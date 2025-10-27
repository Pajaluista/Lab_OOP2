package io;

import functions.TabulatedFunction;
import functions.LinkedListTabulatedFunction;
import operations.TabulatedDifferentialOperator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 Это демонстрационный класс, который:
 Создаёт табулированную функцию f(x) = x³.
 Находит её производные (с помощью TabulatedDifferentialOperator).
 Сохраняет функции в файл (сериализация).
 Считывает функции из файла (десериализация).
 */
public class LinkedListTabulatedFunctionSerialization {

    public static void main(String[] args) {
        // Создаем исходную функцию f(x) = x³
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {1.0, 8.0, 27.0, 64.0, 125.0}; // x³

        LinkedListTabulatedFunction originalFunction = new LinkedListTabulatedFunction(xValues, yValues);

        // Создаем дифференциальный оператор
        TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator();

        // Находим первую производную f'(x) ≈ 3x²
        TabulatedFunction firstDerivative = differentialOperator.derive(originalFunction);

        // Находим вторую производную f''(x) ≈ 6x
        TabulatedFunction secondDerivative = differentialOperator.derive(firstDerivative);

        System.out.println("Исходная функция и производные созданы:");
        System.out.println("f(x) = x³");
        System.out.println("f'(x) ≈ 3x²");
        System.out.println("f''(x) ≈ 6x");

        System.out.println("\n=== СЕРИАЛИЗАЦИЯ ===");
        try (
                FileOutputStream fileOutputStream = new FileOutputStream("output/serialized linked list functions.bin");
                BufferedOutputStream bufferedStream = new BufferedOutputStream(fileOutputStream)
        ) {
            // Сериализуем все три функции в один файл
            FunctionsIO.serialize(bufferedStream, originalFunction);
            FunctionsIO.serialize(bufferedStream, firstDerivative);
            FunctionsIO.serialize(bufferedStream, secondDerivative);

            System.out.println("Функции успешно сериализованы в файл:");
            System.out.println("output/serialized linked list functions.bin");

        } catch (IOException e) {
            System.err.println("Ошибка при сериализации функций:");
            e.printStackTrace();
            return;
        }

        System.out.println("\n=== ДЕСЕРИАЛИЗАЦИЯ ===");
        try (
                FileInputStream fileInputStream = new FileInputStream("output/serialized linked list functions.bin");
                BufferedInputStream bufferedStream = new BufferedInputStream(fileInputStream)
        ) {
            // Десериализуем все три функции в том же порядке
            TabulatedFunction deserializedOriginal = FunctionsIO.deserialize(bufferedStream);
            TabulatedFunction deserializedFirstDerivative = FunctionsIO.deserialize(bufferedStream);
            TabulatedFunction deserializedSecondDerivative = FunctionsIO.deserialize(bufferedStream);

            System.out.println("Функции успешно десериализованы из файла:");

            // Выводим функции в консоль
            System.out.println("\n1. Исходная функция f(x) = x³:");
            System.out.println(deserializedOriginal.toString());

            System.out.println("\n2. Первая производная f'(x) ≈ 3x²:");
            System.out.println(deserializedFirstDerivative.toString());

            System.out.println("\n3. Вторая производная f''(x) ≈ 6x:");
            System.out.println(deserializedSecondDerivative.toString());

            // Проверяем корректность десериализации
            System.out.println("\n=== ПРОВЕРКА КОРРЕКТНОСТИ ===");
            System.out.println("Исходная функция эквивалентна: " + originalFunction.equals(deserializedOriginal));
            System.out.println("Первая производная эквивалентна: " + firstDerivative.equals(deserializedFirstDerivative));
            System.out.println("Вторая производная эквивалентна: " + secondDerivative.equals(deserializedSecondDerivative));

            // Проверяем, что все функции имеют правильный тип
            System.out.println("\n=== ПРОВЕРКА ТИПОВ ===");
            System.out.println("Исходная функция тип: " + deserializedOriginal.getClass().getSimpleName());
            System.out.println("Первая производная тип: " + deserializedFirstDerivative.getClass().getSimpleName());
            System.out.println("Вторая производная тип: " + deserializedSecondDerivative.getClass().getSimpleName());

            // Дополнительная проверка - вычисляем значения в точках
            System.out.println("\n=== ПРОВЕРКА ЗНАЧЕНИЙ ===");
            System.out.println("В точке x = 3.0:");
            System.out.printf("f(3.0) = %.2f (ожидается 27.00)%n", deserializedOriginal.apply(3.0));
            System.out.printf("f'(3.0) = %.2f (ожидается ≈27.00)%n", deserializedFirstDerivative.apply(3.0));
            System.out.printf("f''(3.0) = %.2f (ожидается ≈18.00)%n", deserializedSecondDerivative.apply(3.0));

            System.out.println("\nВ точке x = 4.0:");
            System.out.printf("f(4.0) = %.2f (ожидается 64.00)%n", deserializedOriginal.apply(4.0));
            System.out.printf("f'(4.0) = %.2f (ожидается ≈48.00)%n", deserializedFirstDerivative.apply(4.0));
            System.out.printf("f''(4.0) = %.2f (ожидается ≈24.00)%n", deserializedSecondDerivative.apply(4.0));

        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода при десериализации:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка: класс функции не найден при десериализации:");
            e.printStackTrace();
        }
    }
}