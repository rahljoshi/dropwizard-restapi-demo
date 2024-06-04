package nagarro.exception;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
