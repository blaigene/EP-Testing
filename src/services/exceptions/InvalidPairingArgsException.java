package services.exceptions;

public class InvalidPairingArgsException extends RuntimeException {
    public InvalidPairingArgsException(String message) {
        super(message);
    }
}
