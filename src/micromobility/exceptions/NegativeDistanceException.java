package micromobility.exceptions;

public class NegativeDistanceException extends RuntimeException {
    public NegativeDistanceException(String message) {
        super(message);
    }
}
