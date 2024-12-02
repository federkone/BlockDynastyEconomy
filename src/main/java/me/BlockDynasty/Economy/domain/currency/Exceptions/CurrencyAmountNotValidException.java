package me.BlockDynasty.Economy.domain.currency.Exceptions;

public class CurrencyAmountNotValidException extends CurrencyException {
    public CurrencyAmountNotValidException(String message) {
        super(message);
    }
    public CurrencyAmountNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
