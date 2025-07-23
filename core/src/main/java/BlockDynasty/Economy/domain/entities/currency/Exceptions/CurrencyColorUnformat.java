package BlockDynasty.Economy.domain.entities.currency.Exceptions;

public class CurrencyColorUnformat extends CurrencyException {
    public CurrencyColorUnformat(String message) {
        super(message);
    }

    public CurrencyColorUnformat(String message, Throwable cause) {
        super(message, cause);
    }
}
