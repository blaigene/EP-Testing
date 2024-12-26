package services.exceptions;

public class CorruptedImgException extends RuntimeException {
    public CorruptedImgException(String message) {
        super(message);
    }
}
