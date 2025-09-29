package BlockDynasty.Economy.domain.persistence.Exceptions;

public class RepositoryNotSupportTopException extends RepositoryException {
    public RepositoryNotSupportTopException(String message) {
        super(message);
    }

    public RepositoryNotSupportTopException(String message, Throwable cause) {
        super(message, cause);
    }
}
