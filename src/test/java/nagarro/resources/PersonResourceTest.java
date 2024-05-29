package nagarro.resources;

import jakarta.ws.rs.core.Response;
import nagarro.dto.PersonDTO;
import nagarro.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class PersonResourceTest {
    @Mock
    private PersonServiceImpl personService;

    @InjectMocks
    private PersonResource personResource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPersons() {
        PersonDTO person1 = new PersonDTO(1, "Rahul", 21);
        PersonDTO person2 = new PersonDTO(2, "Bobby", 25);
        when(personService.getAllPersons()).thenReturn(Arrays.asList(person1, person2));

//        Response response = personResource.getAllPersons();
//        List<PersonDTO> persons =response.readEntity(new GenericType<List<PersonDTO>>() {});

        List<PersonDTO> persons = personService.getAllPersons();

        assertEquals(2, persons.size());
        assertEquals("Rahul", persons.get(0).getName());
        assertEquals(25, persons.get(1).getAge());
        Mockito.verify(personService, times(1)).getAllPersons();
    }

    @Test
    public void testGetPersonById() {
        PersonDTO person = new PersonDTO(1, "Rahul", 21);
        when(personService.getPersonById(1)).thenReturn(person);

        Response response = personResource.getPersonById(1);
        PersonDTO retrievedPerson = (PersonDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertNotNull(retrievedPerson);
        assertEquals("Rahul", retrievedPerson.getName());
        assertEquals(21, retrievedPerson.getAge());
        Mockito.verify(personService, times(1)).getPersonById(1);
    }

    @Test
    public void testCreatePerson() {
        PersonDTO personDTO = new PersonDTO(0, "Rahul", 21);
        when(personService.insertPerson(any(PersonDTO.class))).thenReturn(personDTO);

        Response response = personResource.createPerson(personDTO);
        PersonDTO createdPerson = (PersonDTO) response.getEntity();

        assertEquals(201, response.getStatus());
        assertNotNull(createdPerson);
        assertEquals("Rahul", createdPerson.getName());
        assertEquals(21, createdPerson.getAge());
        Mockito.verify(personService, times(1)).insertPerson(any(PersonDTO.class));
    }

    @Test
    public void testUpdatePerson() {
        PersonDTO personDTO = new PersonDTO(1, "Rahul", 21);

        Response response = personResource.updatePerson(1,personDTO);
        assertEquals(200, response.getStatus());
        Mockito.verify(personService, times(1)).updatePerson(any(PersonDTO.class));
    }

    @Test
    public void testDeletePerson() {
        Response response = personResource.deletePerson(1);
        assertEquals(204, response.getStatus());
        Mockito.verify(personService, times(1)).deletePerson(1);
    }
}

