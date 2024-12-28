package micromobility.exceptions;

public class ProceduralException extends Exception {
    public ProceduralException() {
        super("Se ha producido un error en el procedimiento del caso de uso.");
    }

    public ProceduralException(String message) {
        super(message);
    }

    public ProceduralException(String message, Throwable cause) {
        super(message, cause);
    }
}