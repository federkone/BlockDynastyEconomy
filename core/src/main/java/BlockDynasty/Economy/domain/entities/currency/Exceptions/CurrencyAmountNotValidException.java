package BlockDynasty.Economy.domain.entities.currency.Exceptions;

public class CurrencyAmountNotValidException extends CurrencyException {
    public CurrencyAmountNotValidException(String message) {
        super(message);
    }
    public CurrencyAmountNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
