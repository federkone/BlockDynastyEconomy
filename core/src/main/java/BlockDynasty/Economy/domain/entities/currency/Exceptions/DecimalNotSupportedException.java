package BlockDynasty.Economy.domain.entities.currency.Exceptions;

public class DecimalNotSupportedException extends CurrencyException {
    public DecimalNotSupportedException(String message) {
        super(message);
    }
    public DecimalNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}