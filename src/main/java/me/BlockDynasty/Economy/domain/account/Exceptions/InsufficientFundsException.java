package me.BlockDynasty.Economy.domain.account.Exceptions;


public class InsufficientFundsException extends AccountExeption {
    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}