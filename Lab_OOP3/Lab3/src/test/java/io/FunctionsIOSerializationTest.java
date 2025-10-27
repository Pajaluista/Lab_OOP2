package io;

import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class FunctionsIOSerializationTest {

    @Test
    public void testSerializeArrayTabulatedFunction() throws IOException, ClassNotFoundException {
        // Arrange
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        TabulatedFunction original = new ArrayTabulatedFunction(xValues, yValues);

        // Act - сериализация
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedStream = new BufferedOutputStream(byteArrayOutputStream);
        FunctionsIO.serialize(bufferedStream, original);

        // Act - десериализация
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TabulatedFunction deserialized = (TabulatedFunction) objectInputStream.readObject();
        objectInputStream.close();

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
    public void testSerializeLinkedListTabulatedFunction() throws IOException, ClassNotFoundException {
        // Arrange
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};
        TabulatedFunction original = new LinkedListTabulatedFunction(xValues, yValues);

        // Act - сериализация
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedStream = new BufferedOutputStream(byteArrayOutputStream);
        FunctionsIO.serialize(bufferedStream, original);

        // Act - десериализация
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TabulatedFunction deserialized = (TabulatedFunction) objectInputStream.readObject();
        objectInputStream.close();

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
    public void testSerializeWithIOException() {
        // Arrange - создаем закрытый поток для тестирования исключения
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream stream = new BufferedOutputStream(byteArrayOutputStream);

        try {
            stream.close(); // закрываем поток
        } catch (IOException e) {
            fail("Unexpected exception during stream closing");
        }

        TabulatedFunction function = new ArrayTabulatedFunction(new double[]{1, 2}, new double[]{1, 2});

        // Act & Assert - попытка записи в закрытый поток должна бросить IOException
        assertThrows(IOException.class, () -> FunctionsIO.serialize(stream, function));
    }
}