package exceptions;

public class CorruptedImgException extends Exception {
    public CorruptedImgException() {
        super("La imagen del código QR está corrupta o no es válida.");
    }

    public CorruptedImgException(String message) {
        super(message);
    }

    public CorruptedImgException(String message, Throwable cause) {
        super(message, cause);
    }
}