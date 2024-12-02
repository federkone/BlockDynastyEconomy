package me.BlockDynasty.Economy.domain.currency.Exceptions;

public class CurrencyNotFoundException extends CurrencyException {
    public CurrencyNotFoundException(String message) {
        super(message);
    }
    public CurrencyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
