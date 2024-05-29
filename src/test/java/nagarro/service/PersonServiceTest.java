package nagarro.service;

import nagarro.dao.PersonDAO;
import nagarro.dto.PersonDTO;
import nagarro.entity.Person;
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
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    @Mock
    private PersonDAO personDAO;

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPersons() {
        Person person1 = new Person(1, "Rahul", 21);
        Person person2 = new Person(2, "Bobby", 25);
        when(personDAO.getAllPersons()).thenReturn(Arrays.asList(person1, person2));

        List<PersonDTO> persons = personService.getAllPersons();

        assertEquals(2, persons.size());
        assertEquals("Rahul", persons.get(0).getName());
        assertEquals(25, persons.get(1).getAge());
        Mockito.verify(personDAO, times(1)).getAllPersons();
    }

    @Test
    public void testGetPersonById() {
        Person person = new Person(1, "Rahul", 21);
        when(personDAO.getPersonById(1)).thenReturn(person);

        PersonDTO personDTO = personService.getPersonById(1);

        assertNotNull(personDTO);
        assertEquals("Rahul", personDTO.getName());
        assertEquals(21, personDTO.getAge());
        Mockito.verify(personDAO, times(1)).getPersonById(1);
    }

    @Test
    public void testCreatePerson() {
        Person person = new Person(0, "Rahul", 21);
        when(personDAO.insertPerson(any(Person.class))).thenReturn(1);

        PersonDTO personDTO = new PersonDTO(0, "Rahul", 21);
        PersonDTO createdPerson = personService.insertPerson(personDTO);

        assertNotNull(createdPerson);
        assertEquals(1, createdPerson.getId());
        assertEquals("Rahul", createdPerson.getName());
        Mockito.verify(personDAO, times(1)).insertPerson(any(Person.class));
    }

    @Test
    public void testUpdatePerson() {
        PersonDTO personDTO = new PersonDTO(1, "Rahul", 21);
        doNothing().when(personDAO).updatePerson(any(Person.class));

        personService.updatePerson(personDTO);

        Mockito.verify(personDAO, times(1)).updatePerson(any(Person.class));
    }

    @Test
    public void testDeletePerson() {
        doNothing().when(personDAO).deletePerson(1);

        personService.deletePerson(1);

        Mockito.verify(personDAO, times(1)).deletePerson(1);
    }
}
