package BlockDynasty.Economy.domain.entities.currency.Exceptions;

public class CurrencyNotFoundException extends CurrencyException {
    public CurrencyNotFoundException(String message) {
        super(message);
    }
    public CurrencyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
