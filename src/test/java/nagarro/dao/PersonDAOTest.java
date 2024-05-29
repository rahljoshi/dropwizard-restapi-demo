package nagarro.dao;

import nagarro.entity.Person;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.h2.H2DatabasePlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.jdbi.v3.testing.JdbiRule.*;
import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {

    private Jdbi jdbi;
    private PersonDAO personDAO;

    @BeforeEach
    public void setUp() {
        jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.installPlugin(new H2DatabasePlugin());

        try (Handle handle = jdbi.open()) {
            handle.execute("DROP TABLE IF EXISTS person");
            handle.execute("CREATE TABLE person (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), age INTEGER)");
        }
        personDAO = jdbi.onDemand(PersonDAO.class);
    }


    @Test
    public void testInsertAndRetrievePerson() {
        Person person = new Person(0, "Rahul", 21);
        int id = personDAO.insertPerson(person);
        assertTrue(id > 0);

        Person retrievedPerson = personDAO.getPersonById(id);

        assertNotNull(retrievedPerson);
        assertEquals("Rahul", retrievedPerson.getName());
        assertEquals(21, retrievedPerson.getAge());
    }

    @Test
    public void testGetAllPersons() {
        personDAO.insertPerson(new Person(0, "Rahul", 21));
        personDAO.insertPerson(new Person(0, "Bobby", 30));

        List<Person> persons = personDAO.getAllPersons();
        assertEquals(2, persons.size());
    }

    @Test
    public void testUpdatePerson() {
        Person person = new Person(0, "Rahul", 21);
        int id = personDAO.insertPerson(person);

        Person updatedPerson = new Person(id, "Rahul Updated", 31);
        personDAO.updatePerson(updatedPerson);

        Person retrievedPerson = personDAO.getPersonById(id);
        assertNotNull(retrievedPerson);
        assertEquals("Rahul Updated", retrievedPerson.getName());
        assertEquals(31, retrievedPerson.getAge());
    }

    @Test
    public void testDeletePerson() {
        Person person = new Person(0, "Rahul", 21);
        int id = personDAO.insertPerson(person);

        personDAO.deletePerson(id);

        Person retrievedPerson = personDAO.getPersonById(id);
        assertNull(retrievedPerson);
    }
}
