package io;

import functions.TabulatedFunction;
import functions.Point;
import functions.factory.TabulatedFunctionFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Utility-класс для операций ввода-вывода с функциями
 * Не может иметь наследников и экземпляров
 */
public final class FunctionsIO {

    /**
     Это утилитарный класс, который помогает записывать и считывать табулированные функции.
     Он не может иметь экземпляров (private конструктор).
     Он не может наследоваться (final класс).
     Все методы — статические.
     */
    private FunctionsIO() {
        throw new UnsupportedOperationException("Cannot instantiate utility class FunctionsIO");
    }


    //Записывает функцию в текстовый файл.
    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) throws IOException {
        PrintWriter printWriter = new PrintWriter(writer);

        // Записываем количество точек
        printWriter.println(function.getCount());

        // Записываем все точки в цикле for-each
        for (Point point : function) {
            printWriter.printf("%f %f\n", point.x, point.y);
        }

        // Сбрасываем буфер, но не закрываем поток
        printWriter.flush();
    }


    //Записывает функцию в двоичный файл
    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Записываем количество точек
        dataOutputStream.writeInt(function.getCount());

        // Записываем все точки в цикле for-each
        for (Point point : function) {
            dataOutputStream.writeDouble(point.x);
            dataOutputStream.writeDouble(point.y);
        }

        // Сбрасываем буфер, но не закрываем поток
        dataOutputStream.flush();
    }


    //Сохраняет функцию в двоичном файле с помощью сериализации Java.
    public static void serialize(BufferedOutputStream stream, TabulatedFunction function) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);

        // Записываем объект функции
        objectOutputStream.writeObject(function);

        // Сбрасываем буфер, но не закрываем поток
        objectOutputStream.flush();
    }


    public static TabulatedFunction deserialize(BufferedInputStream stream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(stream);

        // Читаем и возвращаем объект функции
        return (TabulatedFunction) objectInputStream.readObject();
    }


    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory) throws IOException {
        // Создаем форматтер для русской локализации (с запятыми)
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.forLanguageTag("ru"));

        try {
            // Читаем количество точек
            String countLine = reader.readLine();
            if (countLine == null) {
                throw new IOException("File is empty");
            }

            int count = Integer.parseInt(countLine.trim());

            // Создаем массивы для хранения данных
            double[] xValues = new double[count];
            double[] yValues = new double[count];

            // Читаем точки
            for (int i = 0; i < count; i++) {
                String pointLine = reader.readLine();
                if (pointLine == null) {
                    throw new IOException("Unexpected end of file at point " + i);
                }

                // Разделяем строку по пробелу
                String[] parts = pointLine.split(" ");
                if (parts.length != 2) {
                    throw new IOException("Invalid format at line " + (i + 2) + ": expected two numbers separated by space");
                }

                // Парсим числа с учетом русской локализации
                try {
                    Number xNumber = numberFormat.parse(parts[0].trim());
                    Number yNumber = numberFormat.parse(parts[1].trim());

                    xValues[i] = xNumber.doubleValue();
                    yValues[i] = yNumber.doubleValue();

                } catch (ParseException e) {
                    throw new IOException("Error parsing numbers at line " + (i + 2) + ": " + e.getMessage(), e);
                }
            }

            // Создаем функцию с помощью фабрики
            return factory.create(xValues, yValues);

        } catch (NumberFormatException e) {
            throw new IOException("Error parsing integer count: " + e.getMessage(), e);
        }
    }
}