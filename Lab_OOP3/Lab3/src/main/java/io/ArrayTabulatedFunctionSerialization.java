package io;

import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import operations.TabulatedDifferentialOperator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
  Это демонстрационный класс, который:
  Создаёт табулированную функцию f(x) = x².
  Находит её производные (с помощью TabulatedDifferentialOperator).
  Сохраняет функции в файл (сериализация).
  Считывает функции из файла (десериализация)
 */
public class ArrayTabulatedFunctionSerialization {

    public static void main(String[] args) {
        // Создаем исходную функцию f(x) = x²
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0, 25.0}; // x²

        ArrayTabulatedFunction originalFunction = new ArrayTabulatedFunction(xValues, yValues);

        // Создаем дифференциальный оператор
        TabulatedDifferentialOperator differentialOperator = new TabulatedDifferentialOperator();

        // Находим первую производную f'(x) = 2x
        TabulatedFunction firstDerivative = differentialOperator.derive(originalFunction);

        // Находим вторую производную f''(x) = 2
        TabulatedFunction secondDerivative = differentialOperator.derive(firstDerivative);

        System.out.println("Исходная функция и производные созданы:");
        System.out.println("f(x) = x²");
        System.out.println("f'(x) ≈ 2x");
        System.out.println("f''(x) ≈ 2");


        System.out.println("\n=== СЕРИАЛИЗАЦИЯ ===");
        try (
                FileOutputStream fileOutputStream = new FileOutputStream("output/serialized array functions.bin");
                BufferedOutputStream bufferedStream = new BufferedOutputStream(fileOutputStream)
        ) {
            // Сериализуем все три функции в один файл
            FunctionsIO.serialize(bufferedStream, originalFunction);
            FunctionsIO.serialize(bufferedStream, firstDerivative);
            FunctionsIO.serialize(bufferedStream, secondDerivative);

            System.out.println("Функции успешно сериализованы в файл:");
            System.out.println("output/serialized array functions.bin");

        } catch (IOException e) {
            System.err.println("Ошибка при сериализации функций:");
            e.printStackTrace();
            return;
        }


        System.out.println("\n=== ДЕСЕРИАЛИЗАЦИЯ ===");
        try (
                FileInputStream fileInputStream = new FileInputStream("output/serialized array functions.bin");
                BufferedInputStream bufferedStream = new BufferedInputStream(fileInputStream)
        ) {
            // Десериализуем все три функции в том же порядке
            TabulatedFunction deserializedOriginal = FunctionsIO.deserialize(bufferedStream);
            TabulatedFunction deserializedFirstDerivative = FunctionsIO.deserialize(bufferedStream);
            TabulatedFunction deserializedSecondDerivative = FunctionsIO.deserialize(bufferedStream);

            System.out.println("Функции успешно десериализованы из файла:");

            // Выводим функции в консоль
            System.out.println("\n1. Исходная функция f(x) = x²:");
            System.out.println(deserializedOriginal.toString());

            System.out.println("\n2. Первая производная f'(x) ≈ 2x:");
            System.out.println(deserializedFirstDerivative.toString());

            System.out.println("\n3. Вторая производная f''(x) ≈ 2:");
            System.out.println(deserializedSecondDerivative.toString());

            // Проверяем корректность десериализации
            System.out.println("\n=== ПРОВЕРКА КОРРЕКТНОСТИ ===");
            System.out.println("Исходная функция эквивалентна: " + originalFunction.equals(deserializedOriginal));
            System.out.println("Первая производная эквивалентна: " + firstDerivative.equals(deserializedFirstDerivative));
            System.out.println("Вторая производная эквивалентна: " + secondDerivative.equals(deserializedSecondDerivative));

            // Дополнительная проверка - вычисляем значения в точках
            System.out.println("\n=== ПРОВЕРКА ЗНАЧЕНИЙ ===");
            System.out.println("В точке x = 3.0:");
            System.out.printf("f(3.0) = %.2f (ожидается 9.00)%n", deserializedOriginal.apply(3.0));
            System.out.printf("f'(3.0) = %.2f (ожидается ≈6.00)%n", deserializedFirstDerivative.apply(3.0));
            System.out.printf("f''(3.0) = %.2f (ожидается ≈2.00)%n", deserializedSecondDerivative.apply(3.0));

        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода при десериализации:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка: класс функции не найден при десериализации:");
            e.printStackTrace();
        }
    }
}