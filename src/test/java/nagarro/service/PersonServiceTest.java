package nagarro.service;

import nagarro.dao.PersonDAO;
import nagarro.dto.PersonDTO;
import nagarro.entity.Person;
import nagarro.exception.PersonNotFoundException;
import nagarro.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    private static final String PERSON_NAME = "Rahul";
    private static final int PERSON_AGE = 21;
    private static final int PERSON_ID = 1;
    private static final int INVALID_ID = -1;

    @Mock
    private PersonDAO personDAO;

    private PersonService personService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        personService = new PersonServiceImpl(personDAO);
    }

    /**
     * Positive test cases
     */

    @Test
    @DisplayName("Get all persons")
    void testGetAllPersons() {
        // Given
        final var person1 = new Person(PERSON_ID, PERSON_NAME, PERSON_AGE);
        final var person2 = new Person(2, "Bobby", 25);
        when(personDAO.getAllPersons()).thenReturn(asList(person1, person2));

        // When
        final var persons = personService.getAllPersons();

        // Then
        assertEquals(2, persons.size());
        assertEquals(PERSON_NAME, persons.get(0).getName());
        assertEquals(25, persons.get(1).getAge());
        verify(personDAO).getAllPersons();
    }

    @Test
    @DisplayName("Get person by ID")
    void testGetPersonById() {
        // Given
        final var person = new Person(PERSON_ID, PERSON_NAME, PERSON_AGE);
        when(personDAO.getPersonById(PERSON_ID)).thenReturn(Optional.of(person));

        // When
        final var personResult = personService.getPersonById(PERSON_ID);

        // Then
        assertNotNull(personResult);
        assertEquals(PERSON_NAME, personResult.getName());
        assertEquals(PERSON_AGE, personResult.getAge());
        verify(personDAO).getPersonById(PERSON_ID);
    }

    @Test
    @DisplayName("Create person with valid data")
    void testCreatePerson() {
        // Given
        final var personDTO = new PersonDTO(0, PERSON_NAME, PERSON_AGE);
        final var expectedPerson = new Person(0, PERSON_NAME, PERSON_AGE);
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        when(personDAO.createPerson(personCaptor.capture())).thenReturn(PERSON_ID);

        // When
        final var createdPerson = personService.createPerson(personDTO);

        // Then
        assertNotNull(createdPerson);
        assertEquals(PERSON_ID, createdPerson.getId());
        assertEquals(PERSON_NAME, createdPerson.getName());
        assertEquals(PERSON_AGE, createdPerson.getAge());

        Person capturedPerson = personCaptor.getValue();
        assertEquals(expectedPerson.getName(), capturedPerson.getName());
        assertEquals(expectedPerson.getAge(), capturedPerson.getAge());
    }

    @Test
    @DisplayName("Update person with valid data")
    void testUpdatePerson() {
        // Given
        final var personDTO = new PersonDTO(PERSON_ID, PERSON_NAME, PERSON_AGE);
        when(personDAO.getPersonById(PERSON_ID)).thenReturn(Optional.of(new Person(PERSON_ID, PERSON_NAME, PERSON_AGE)));
        doNothing().when(personDAO).updatePerson(any(Person.class));

        // When
        personService.updatePerson(PERSON_ID, personDTO);

        // Then
        verify(personDAO).updatePerson(refEq(new Person(PERSON_ID, PERSON_NAME, PERSON_AGE)));
    }

    @Test
    @DisplayName("Delete person with valid ID")
    void testDeletePerson() {
        // give
        final var idToDelete = 1;
        when(personDAO.getPersonById(idToDelete)).thenReturn(Optional.of(new Person(idToDelete, "John", 30)));

        // when
        personService.deletePerson(idToDelete);

        // then
        verify(personDAO).deletePerson(idToDelete);
    }

    // Negative test cases

    @Test
    @DisplayName("Get all persons when none exist")
    void testGetAllPersonsWhenNoneExist() {
        // Given
        when(personDAO.getAllPersons()).thenReturn(emptyList());

        // When
        final var persons = personService.getAllPersons();

        // Then
        assertTrue(persons.isEmpty());
        verify(personDAO).getAllPersons();
    }

    @Test
    @DisplayName("Get person by invalid ID")
    void testGetPersonByIdWithInvalidId() {
        // Given
        when(personDAO.getPersonById(INVALID_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PersonNotFoundException.class, () -> personService.getPersonById(INVALID_ID));
    }

    @Test
    @DisplayName("Delete person with invalid ID")
    void testDeletePersonWithInvalidId() {
        // when & then
        assertThrows(PersonNotFoundException.class, () -> personService.deletePerson(INVALID_ID));
    }

    @Test
    @DisplayName("Delete person not present in database")
    void testDeletePersonNotPresent() {
        // Given
        final var idToDelete = 1;
        when(personDAO.getPersonById(idToDelete)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PersonNotFoundException.class, () -> personService.deletePerson(idToDelete));
    }

    @Test
    @DisplayName("Create person with null data")
    void testCreatePersonWithNullData() {
        // When & Then
        assertThrows(NullPointerException.class, () -> personService.createPerson(null));
    }
}
