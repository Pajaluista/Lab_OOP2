package exceptions;

public class InterpolationException extends RuntimeException {
    public InterpolationException() {
        super();
    }

    public InterpolationException(String message) {
        super(message);
    }
}

//выбрасывается, когда попытка интерполяции происходит вне допустимого интервала (например, x не находится между x[floorIndex] и x[floorIndex + 1]).