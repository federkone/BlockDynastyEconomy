package me.BlockDynasty.Economy.domain.entities.account.Exceptions;

public class AccountCanNotReciveException extends AccountExeption {
    public AccountCanNotReciveException(String message) {
        super(message);
    }

    public AccountCanNotReciveException(String message, Throwable cause) {
        super(message, cause);
    }
}
