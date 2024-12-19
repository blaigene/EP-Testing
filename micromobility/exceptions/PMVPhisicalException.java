package micromobility.exceptions;

public class PMVPhisicalException extends Exception {
    public PMVPhisicalException() {
        super("Se ha producido un problema físico con el vehículo de movilidad personal (PMV).");
    }

    public PMVPhisicalException(String message) {
        super(message);
    }

    public PMVPhisicalException(String message, Throwable cause) {
        super(message, cause);
    }
}