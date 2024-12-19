package micromobility.exceptions;

public class PMVNotAvailException extends Exception {

    public PMVNotAvailException() {
        super("El vehículo de movilidad personal (PMV) no está disponible.");
    }

    public PMVNotAvailException(String message) {
        super(message);
    }

    public PMVNotAvailException(String message, Throwable cause) {
        super(message, cause);
    }
}