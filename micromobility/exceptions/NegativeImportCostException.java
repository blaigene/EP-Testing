package micromobility.exceptions;

public class NegativeImportCostException extends RuntimeException {
    public NegativeImportCostException(String message) {
        super(message);
    }
}
