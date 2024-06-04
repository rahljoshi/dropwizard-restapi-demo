package nagarro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nagarro.exception.CustomServiceException;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PersonDTO {
    private int id;
    private String name;
    private int age;

    public void validateForUpdate() {
        if (name == null || name.isEmpty() || age < 0) {
            throw new CustomServiceException(BAD_REQUEST, "Invalid person data for updating");
        }
    }

    public void validateForCreation() {
        if (name == null || name.isEmpty() || age < 0) {
            throw new CustomServiceException(BAD_REQUEST, "Person data for creation cannot be empty");
        }
    }
}
