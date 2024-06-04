package nagarro.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class CustomServiceException extends RuntimeException {
    private final Response.Status status;

    public CustomServiceException(Response.Status status, String message) {
        super(message);
        this.status = status;
    }

}