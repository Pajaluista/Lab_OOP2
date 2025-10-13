package io;

import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class FunctionsIODeserializationTest {

    @Test
    public void testDeserializeArrayTabulatedFunction() throws IOException, ClassNotFoundException {
        // Arrange - создаем и сериализуем функцию
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);

        // Сериализуем
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream outputStream = new BufferedOutputStream(byteArrayOutputStream);
        FunctionsIO.serialize(outputStream, original);

        // Act - десериализуем
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BufferedInputStream inputStream = new BufferedInputStream(byteArrayInputStream);
        TabulatedFunction deserialized = FunctionsIO.deserialize(inputStream);

        // Assert
        assertNotNull(deserialized);
        assertTrue(deserialized instanceof ArrayTabulatedFunction);
        assertEquals(original.getCount(), deserialized.getCount());

        for (int i = 0; i < original.getCount(); i++) {
            assertEquals(original.getX(i), deserialized.getX(i), 1e-10);
            assertEquals(original.getY(i), deserialized.getY(i), 1e-10);
        }
    }

    @Test
    public void testDeserializeLinkedListTabulatedFunction() throws IOException, ClassNotFoundException {
        // Arrange - создаем и сериализуем функцию
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        TabulatedFunction original = new LinkedListTabulatedFunction(xValues, yValues);

        // Сериализуем
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream outputStream = new BufferedOutputStream(byteArrayOutputStream);
        FunctionsIO.serialize(outputStream, original);

        // Act - десериализуем
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BufferedInputStream inputStream = new BufferedInputStream(byteArrayInputStream);
        TabulatedFunction deserialized = FunctionsIO.deserialize(inputStream);

        // Assert
        assertNotNull(deserialized);
        assertTrue(deserialized instanceof LinkedListTabulatedFunction);
        assertEquals(original.getCount(), deserialized.getCount());

        for (int i = 0; i < original.getCount(); i++) {
            assertEquals(original.getX(i), deserialized.getX(i), 1e-10);
            assertEquals(original.getY(i), deserialized.getY(i), 1e-10);
        }
    }

    @Test
    public void testDeserializeWithIOException() {
        // Arrange - создаем закрытый поток для тестирования исключения
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        BufferedInputStream stream = new BufferedInputStream(byteArrayInputStream);

        try {
            stream.close(); // закрываем поток
        } catch (IOException e) {
            fail("Unexpected exception during stream closing");
        }

        // Act & Assert - попытка чтения из закрытого потока должна бросить IOException
        assertThrows(IOException.class, () -> FunctionsIO.deserialize(stream));
    }

    @Test
    public void testDeserializeWithClassNotFoundException() throws IOException {
        // Arrange - создаем поток с некорректными данными
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject("This is not a TabulatedFunction"); // записываем строку вместо функции
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BufferedInputStream stream = new BufferedInputStream(byteArrayInputStream);

        // Act & Assert - попытка десериализации некорректного объекта должна бросить ClassNotFoundException
        assertThrows(ClassNotFoundException.class, () -> FunctionsIO.deserialize(stream));
    }
}