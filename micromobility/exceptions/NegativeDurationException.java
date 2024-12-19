package micromobility.exceptions;

public class NegativeDurationException extends RuntimeException {
    public NegativeDurationException(String message) {
        super(message);
    }
}
