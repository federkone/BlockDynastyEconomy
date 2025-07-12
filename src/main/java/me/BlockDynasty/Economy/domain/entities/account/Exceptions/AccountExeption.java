package me.BlockDynasty.Economy.domain.entities.account.Exceptions;

public class AccountExeption extends RuntimeException {
        public AccountExeption(String message) {
            super(message);
        }

        public AccountExeption(String message, Throwable cause) {
            super(message, cause);
        }
    }