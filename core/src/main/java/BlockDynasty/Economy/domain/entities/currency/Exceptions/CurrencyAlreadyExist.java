package BlockDynasty.Economy.domain.entities.currency.Exceptions;

public class CurrencyAlreadyExist extends CurrencyException {
    public CurrencyAlreadyExist(String message) {
        super(message);
    }

    public CurrencyAlreadyExist(String message, Throwable cause) {
        super(message, cause);
    }
}
