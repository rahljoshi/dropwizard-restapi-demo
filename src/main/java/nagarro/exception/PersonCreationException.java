package nagarro.exception;

public class PersonCreationException extends RuntimeException {
    public PersonCreationException(String message) {
        super(message);
    }

    public PersonCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
