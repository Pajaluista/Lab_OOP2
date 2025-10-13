package exceptions;

public class InconsistentFunctionsException extends RuntimeException {
    public InconsistentFunctionsException() {
        super();
    }

    public InconsistentFunctionsException(String message) {
        super(message);
    }
}
//выбрасывается, когда две табулированные функции не могут быть использованы в бинарных операциях (например, сложении, вычитании)