package micromobility.exceptions;

public class PairingNotFoundException extends Exception {
    public PairingNotFoundException() {
        super("No se encontró la información de emparejamiento.");
    }

    public PairingNotFoundException(String message) {
        super(message);
    }

    public PairingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}