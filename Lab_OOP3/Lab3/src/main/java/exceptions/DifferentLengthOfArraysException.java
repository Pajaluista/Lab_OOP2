package exceptions;

public class DifferentLengthOfArraysException extends RuntimeException {
    public DifferentLengthOfArraysException() {
        super();
    }

    public DifferentLengthOfArraysException(String message) {
        super(message);
    }
}
//выбрасывается, когда длины массивов xValues и yValues не совпадают (например, в конструкторе ArrayTabulatedFunction или LinkedListTabulatedFunction