package nagarro.resources;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nagarro.dto.PersonDTO;
import nagarro.exception.PersonNotFoundException;
import nagarro.service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jetty.http.HttpStatus.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class PersonResourceTest {

    private static final String PERSON_NAME_RAHUL = "Rahul";
    private static final String PERSON_NAME_BOBBY = "Bobby";
    private static final int PERSON_AGE_21 = 21;
    private static final int PERSON_AGE_25 = 25;
    private static final int INVALID_ID = -1;
    private final static PersonService personService = mock(PersonService.class);
    private final ResourceExtension resourceTestRule = setUpResources();

    private String getBaseURL() {
        return "http://localhost:8080";
    }

    private ResourceExtension setUpResources() {
        return ResourceExtension.builder().addResource(new PersonResource(personService)).build();
    }

    private Client client() {
        return resourceTestRule.client();
    }

    @Test
    @DisplayName("Get all persons returns correct data")
    void testGetAllPersons() {
        // Given
        final var person1 = new PersonDTO(1, PERSON_NAME_RAHUL, PERSON_AGE_21);
        final var person2 = new PersonDTO(2, PERSON_NAME_BOBBY, PERSON_AGE_25);
        when(personService.getAllPersons()).thenReturn(List.of(person1, person2));

        // When
        final var response = client().target(getBaseURL()).path("/persons").request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(OK_200);
        final var responsePersons = response.readEntity(new GenericType<List<PersonDTO>>() {
        });
        assertThat(responsePersons).hasSize(2);
        assertThat(responsePersons.get(0).getName()).isEqualTo(PERSON_NAME_RAHUL);
        assertThat(responsePersons.get(1).getAge()).isEqualTo(PERSON_AGE_25);
    }

    @Test
    @DisplayName("Get person by ID returns correct person")
    void testGetPersonById() throws PersonNotFoundException {
        // Given
        final var person = new PersonDTO(1, PERSON_NAME_RAHUL, PERSON_AGE_21);
        when(personService.getPersonById(1)).thenReturn(person);

        // When
        final var response = client().target(getBaseURL()).path("/persons/1").request().get();

        // Then
        assertThat(response.getStatus()).isEqualTo(OK_200);
        final var retrievedPerson = response.readEntity(PersonDTO.class);
        assertThat(retrievedPerson).isNotNull();
        assertThat(retrievedPerson.getName()).isEqualTo(PERSON_NAME_RAHUL);
        assertThat(retrievedPerson.getAge()).isEqualTo(PERSON_AGE_21);
    }

    @Test
    @DisplayName("Create person successfully")
    void testCreatePerson() {
        // Given
        final var personDTO = new PersonDTO(0, PERSON_NAME_RAHUL, PERSON_AGE_21);
        when(personService.createPerson(any(PersonDTO.class))).thenReturn(personDTO);

        // When
        final var response = client().target(getBaseURL()).path("/persons").request().post(Entity.json(personDTO));

        // Then
        assertThat(response.getStatus()).isEqualTo(CREATED_201);
        final var createdPerson = response.readEntity(PersonDTO.class);
        assertThat(createdPerson).isNotNull();
        assertThat(createdPerson.getName()).isEqualTo(PERSON_NAME_RAHUL);
        assertThat(createdPerson.getAge()).isEqualTo(PERSON_AGE_21);
    }

    @Test
    @DisplayName("Update person successfully")
    void testUpdatePerson() throws PersonNotFoundException {
        // Given
        final var personDTO = new PersonDTO(2, PERSON_NAME_RAHUL, PERSON_AGE_21);

        // When
        final var response = client().target(getBaseURL()).path("/persons/2").request().put(Entity.json(personDTO));

        // Then
        assertThat(response.getStatus()).isEqualTo(OK_200);
    }

    @Test
    @DisplayName("Delete person successfully")
    void testDeletePerson() throws PersonNotFoundException {
        // When
        final var response = client().target(getBaseURL()).path("/persons/1").request().delete();

        // Then
        assertThat(response.getStatus()).isEqualTo(NO_CONTENT_204);
    }

    /**
     * Negative test cases
     */


    @Test
    @DisplayName("Get person by invalid ID returns not found")
    void testGetPersonByInvalidId() throws PersonNotFoundException {
        // Given
        when(personService.getPersonById(INVALID_ID)).thenThrow(new PersonNotFoundException("Person not found"));

        // When
        final var response = client().target(getBaseURL()).path("/persons/" + INVALID_ID).request().get();

        // Then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND_404);
    }

    @Test
    @DisplayName("Create person with null data throws exception")
    void testCreatePersonWithNullData() {
        // When
        final var response = client().target(getBaseURL()).path("/persons").request().post(Entity.json(null));

        // Then
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST_400);
    }

    @Test
    @DisplayName("Update person with invalid ID returns not found")
    void testUpdatePersonWithInvalidId() throws PersonNotFoundException {
        // Given
        final var personDTO = new PersonDTO(INVALID_ID, PERSON_NAME_RAHUL, PERSON_AGE_21);
        doThrow(new PersonNotFoundException("Person not found")).when(personService).updatePerson(eq(INVALID_ID), any());

        // When
        final var response = client().target(getBaseURL()).path("/persons/" + INVALID_ID).request().put(Entity.json(personDTO));

        // Then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND_404);
    }

    @Test
    @DisplayName("Update person with invalid data returns bad request")
    void testUpdatePersonWithInvalidData() {
        // Given
        final var personDTO = new PersonDTO(1, PERSON_NAME_RAHUL, PERSON_AGE_21);
        doThrow(new ConstraintViolationException("Person data for update cannot be null", Set.of())).when(personService).updatePerson(eq(1), any());

        // When
        final var response = client().target(getBaseURL()).path("/persons/" + 1).request().put(Entity.json(personDTO));

        // Then
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST_400);
    }

    @Test
    @DisplayName("Delete person with invalid ID returns not found")
    void testDeletePersonWithInvalidId() throws PersonNotFoundException {
        // Given
        doThrow(new PersonNotFoundException("Person not found")).when(personService).deletePerson(100);

        // When
        final var response = client().target(getBaseURL()).path("/persons/" + 100).request().delete();

        // Then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND_404);
    }

    @Test
    @DisplayName("Update person with null data throws bad request")
    void testUpdatePersonWithNullData() {
        // Given
        doThrow(new ConstraintViolationException("Person data for update cannot be null", Set.of())).when(personService).updatePerson(eq(1), any());
        // When
        final var response = client().target(getBaseURL()).path("/persons/1").request().put(Entity.json(new PersonDTO()));

        // Then
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST_400);
    }

}