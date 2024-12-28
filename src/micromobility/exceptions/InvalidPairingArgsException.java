package micromobility.exceptions;

public class InvalidPairingArgsException extends Exception {
    public InvalidPairingArgsException() {
        super("Los argumentos proporcionados para el emparejamiento son inválidos.");
    }

    public InvalidPairingArgsException(String message) {
        super(message);
    }

    public InvalidPairingArgsException(String message, Throwable cause) {
        super(message, cause);
    }
}