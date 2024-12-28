package services.exceptions;

public class MethodNotSupportedException extends RuntimeException {
    public MethodNotSupportedException(String message) {
        super(message);
    }
}
