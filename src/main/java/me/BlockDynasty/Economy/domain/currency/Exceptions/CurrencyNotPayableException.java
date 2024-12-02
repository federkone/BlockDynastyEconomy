package me.BlockDynasty.Economy.domain.currency.Exceptions;

public class CurrencyNotPayableException extends CurrencyException {
    public CurrencyNotPayableException(String message) {
        super(message);
    }
    public CurrencyNotPayableException(String message, Throwable cause) {
        super(message, cause);
    }
}
