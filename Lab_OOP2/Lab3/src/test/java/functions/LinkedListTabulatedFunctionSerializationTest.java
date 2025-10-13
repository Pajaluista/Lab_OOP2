package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class LinkedListTabulatedFunctionSerializationTest {

    @Test
    public void testSerializationDeserialization() throws IOException, ClassNotFoundException {
        // Arrange
        double[] xValues = {1.0, 2.0, 3.0, 4.0};
        double[] yValues = {1.0, 4.0, 9.0, 16.0};
        LinkedListTabulatedFunction original = new LinkedListTabulatedFunction(xValues, yValues);

        // Act - сериализация
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(original);
        objectOutputStream.close();

        // Act - десериализация
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        LinkedListTabulatedFunction deserialized = (LinkedListTabulatedFunction) objectInputStream.readObject();
        objectInputStream.close();

        // Assert
        assertNotNull(deserialized);
        assertEquals(original.getCount(), deserialized.getCount());
        assertEquals(original.leftBound(), deserialized.leftBound(), 1e-10);
        assertEquals(original.rightBound(), deserialized.rightBound(), 1e-10);

        for (int i = 0; i < original.getCount(); i++) {
            assertEquals(original.getX(i), deserialized.getX(i), 1e-10);
            assertEquals(original.getY(i), deserialized.getY(i), 1e-10);
        }

        // Проверяем циклическую структуру
        assertEquals(deserialized.getX(0), deserialized.leftBound(), 1e-10);
        assertEquals(deserialized.getX(deserialized.getCount() - 1), deserialized.rightBound(), 1e-10);
    }
}