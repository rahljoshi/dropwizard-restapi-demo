package nagarro.resources;

import jakarta.ws.rs.core.Response;
import nagarro.dto.PersonDTO;
import nagarro.service.PersonService;
import nagarro.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonResourceTest {

    private static final String PERSON_NAME_RAHUL = "Rahul";
    private static final String PERSON_NAME_BOBBY = "Bobby";
    private static final int PERSON_AGE_21 = 21;
    private static final int PERSON_AGE_25 = 25;
    private static final int STATUS_OK = 200;
    private static final int STATUS_CREATED = 201;
    private static final int STATUS_NO_CONTENT = 204;
    private static final int INVALID_ID = -1;
    private static final int STATUS_NOT_FOUND = 404;

    private final PersonService personService = mock(PersonServiceImpl.class);

    @InjectMocks
    private PersonResource personResource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Get all persons returns correct data")
    void testGetAllPersons() {
        // Given
        final var person1 = new PersonDTO(1, PERSON_NAME_RAHUL, PERSON_AGE_21);
        final var person2 = new PersonDTO(2, PERSON_NAME_BOBBY, PERSON_AGE_25);
        when(personService.getAllPersons()).thenReturn(Arrays.asList(person1, person2));

        // When
        final var response = personResource.getAllPersons();

        // Then
        assertThat(response.getStatus()).isEqualTo(STATUS_OK);
        final var responsePersons = (List<PersonDTO>) response.getEntity();
        assertThat(responsePersons).hasSize(2);
        assertThat(responsePersons.get(0).getName()).isEqualTo(PERSON_NAME_RAHUL);
        assertThat(responsePersons.get(1).getAge()).isEqualTo(PERSON_AGE_25);

        verify(personService).getAllPersons();
    }

    @Test
    @DisplayName("Get person by ID returns correct person")
    void testGetPersonById() {
        // Given
        final var person = new PersonDTO(1, PERSON_NAME_RAHUL, PERSON_AGE_21);
        when(personService.getPersonById(1)).thenReturn(person);

        // When
        final var response = personResource.getPersonById(1);

        // Then
        assertThat(response.getStatus()).isEqualTo(STATUS_OK);
        final var retrievedPerson = (PersonDTO) response.getEntity();
        assertThat(retrievedPerson).isNotNull();
        assertThat(retrievedPerson.getName()).isEqualTo(PERSON_NAME_RAHUL);
        assertThat(retrievedPerson.getAge()).isEqualTo(PERSON_AGE_21);

        verify(personService).getPersonById(1);
    }

    @Test
    @DisplayName("Create person successfully")
    void testCreatePerson() {
        // Given
        final var personDTO = new PersonDTO(0, PERSON_NAME_RAHUL, PERSON_AGE_21);
        when(personService.createPerson(any(PersonDTO.class))).thenReturn(personDTO);

        // When
        final var response = personResource.createPerson(personDTO);

        // Then
        assertThat(response.getStatus()).isEqualTo(STATUS_CREATED);
        final var createdPerson = (PersonDTO) response.getEntity();
        assertThat(createdPerson).isNotNull();
        assertThat(createdPerson.getName()).isEqualTo(PERSON_NAME_RAHUL);
        assertThat(createdPerson.getAge()).isEqualTo(PERSON_AGE_21);

        verify(personService).createPerson(any(PersonDTO.class));
    }

    @Test
    @DisplayName("Update person successfully")
    void testUpdatePerson() {
        // given
        final var personDTO = new PersonDTO(1, PERSON_NAME_RAHUL, PERSON_AGE_21);

        //when
        final var response = personResource.updatePerson(1, personDTO);

        //then
        assertThat(response.getStatus()).isEqualTo(STATUS_OK);
        verify(personService).updatePerson(1, personDTO);
    }

    @Test
    @DisplayName("Delete person successfully")
    void testDeletePerson() {
        // when
        final var response = personResource.deletePerson(1);

        //then
        assertThat(response.getStatus()).isEqualTo(STATUS_NO_CONTENT);
        verify(personService).deletePerson(1);
    }

    /*
        Negative test cases
     */

    @Test
    @DisplayName("Get person by invalid ID returns not found")
    void testGetPersonByInvalidId() {
        // Given
        when(personService.getPersonById(INVALID_ID)).thenReturn(null);

        // When
        final Response response = personResource.getPersonById(INVALID_ID);

        // Then
        assertThat(response.getStatus()).isEqualTo(STATUS_NOT_FOUND);
        verify(personService).getPersonById(INVALID_ID);
    }

    @Test
    @DisplayName("Create person with null data throws exception")
    void testCreatePersonWithNullData() {
        // Given
        when(personService.createPerson(null)).thenThrow(new IllegalArgumentException("Person data cannot be null"));

        // When
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> personResource.createPerson(null));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Person data cannot be null");
    }

    @Test
    @DisplayName("Update person with invalid ID returns not found")
    void testUpdatePersonWithInvalidId() {
        // Given
        final var personDTO = new PersonDTO(INVALID_ID, PERSON_NAME_RAHUL, PERSON_AGE_21);
        doThrow(new IllegalArgumentException("Invalid person ID")).when(personService).updatePerson(INVALID_ID, personDTO);

        // When
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> personResource.updatePerson(INVALID_ID, personDTO));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Invalid person ID");
    }

    @Test
    @DisplayName("Delete person with invalid ID returns not found")
    void testDeletePersonWithInvalidId() {
        // Given
        doThrow(new IllegalArgumentException("Invalid person ID")).when(personService).deletePerson(INVALID_ID);

        // When
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> personResource.deletePerson(INVALID_ID));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Invalid person ID");
    }
}

