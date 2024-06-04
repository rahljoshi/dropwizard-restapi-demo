package nagarro.dao;

import nagarro.entity.Person;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.h2.H2DatabasePlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;


class PersonDAOTest {

    private static final String PERSON_NAME_RAHUL = "Rahul";
    private static final int PERSON_AGE_21 = 21;
    private static final int PERSON_AGE_25 = 25;

    @Mock
    private PersonDAO personDAO;

    @BeforeEach
    public void setUp() {
        final var jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.installPlugin(new H2DatabasePlugin());

        try (var handle = jdbi.open()) {
            handle.execute("DROP TABLE IF EXISTS person");
            handle.execute("CREATE TABLE person (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), age INTEGER)");
        }
        personDAO = jdbi.onDemand(PersonDAO.class);
    }

    @Test
    @DisplayName("Create table successfully")
    void testCreateTable() {
        // When
        personDAO.createTable();

        // Then
        assertTrue(true);
    }


    @Test
    @DisplayName("Create person and retrieve successfully")
    void testInsertAndRetrievePerson() {
        //given
        final var person = new Person(0, PERSON_NAME_RAHUL, PERSON_AGE_21);

        //when
        final var id = personDAO.createPerson(person);

        //then
        assertTrue(id > 0);

        // when
        final var retrievedPerson = personDAO.getPersonById(id);

        //then
        assertTrue(retrievedPerson.isPresent());
        assertThat(retrievedPerson.get().getName(), is(PERSON_NAME_RAHUL));
        assertThat(retrievedPerson.get().getAge(), is(PERSON_AGE_21));
    }

    @Test
    @DisplayName("Get all persons return coorrects data")
    void testGetAllPersons() {
        // given
        personDAO.createPerson(new Person(0, PERSON_NAME_RAHUL, PERSON_AGE_21));
        personDAO.createPerson(new Person(0, PERSON_NAME_RAHUL, PERSON_AGE_25));

        // when
        final var persons = personDAO.getAllPersons();

        // Then
        assertEquals(2, persons.size());
    }

    @Test
    @DisplayName("Update Person Successfully")
    void testUpdatePerson() {
        // Given
        final var person = new Person(0, PERSON_NAME_RAHUL, PERSON_AGE_21);
        final var id = personDAO.createPerson(person);
        final var updatedPerson = new Person(id, "Rahul Updated", 31);

        // When
        personDAO.updatePerson(updatedPerson);

        // Then
        final var retrievedPerson = personDAO.getPersonById(id);
        assertFalse(retrievedPerson.isEmpty());
        assertEquals("Rahul Updated", retrievedPerson.get().getName());
        assertEquals(31, retrievedPerson.get().getAge());
    }

    @Test
    @DisplayName("Delete person successfully")
    void testDeletePerson() {
        // Given
        final var person = new Person(0, PERSON_NAME_RAHUL, PERSON_AGE_21);
        final var id = personDAO.createPerson(person);

        // When
        personDAO.deletePerson(id);

        // Then
        final var retrievedPerson = personDAO.getPersonById(id);
        assertTrue(retrievedPerson.isEmpty());
    }
}
