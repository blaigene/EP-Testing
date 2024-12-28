package micromobility.exceptions;

public class NotEnoughWalletException extends RuntimeException {
    public NotEnoughWalletException(String message) {
        super(message);
    }
}
