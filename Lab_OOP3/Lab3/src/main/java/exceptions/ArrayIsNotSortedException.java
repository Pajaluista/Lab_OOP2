package exceptions;// для исключений пакет

public class ArrayIsNotSortedException extends RuntimeException {//это необъявляемое исключение, то есть его не нужно объявлять в throws у методов.
    public ArrayIsNotSortedException() {
        super();
    }

    public ArrayIsNotSortedException(String message) {
        super(message);
    }
}