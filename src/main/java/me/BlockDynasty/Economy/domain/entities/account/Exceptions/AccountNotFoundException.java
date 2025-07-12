package me.BlockDynasty.Economy.domain.entities.account.Exceptions;

public class AccountNotFoundException extends AccountExeption{
    public AccountNotFoundException(String message) {
        super(message);
    }
    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}