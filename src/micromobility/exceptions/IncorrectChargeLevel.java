package micromobility.exceptions;

public class IncorrectChargeLevel extends RuntimeException {
    public IncorrectChargeLevel(String message) {
        super(message);
    }
}
