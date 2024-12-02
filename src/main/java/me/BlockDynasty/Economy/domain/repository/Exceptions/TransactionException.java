package me.BlockDynasty.Economy.domain.repository.Exceptions;

    public class TransactionException extends RepositoryException {
        public TransactionException(String message) {
            super(message);
        }

        public TransactionException(String message, Throwable cause) {
            super(message, cause);
        }
    }