package nagarro.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import nagarro.dto.ErrorMessage;
import nagarro.exception.PersonNotFoundException;

@Provider
public class PersonNotFoundExceptionMapper implements ExceptionMapper<PersonNotFoundException> {
    @Override
    public Response toResponse(PersonNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessage(exception.getMessage()))
                .build();
    }
}
