package nagarro.dao.impl;

import nagarro.dao.PersonDAO;
import nagarro.dao.mapper.PersonMapper;
import nagarro.entity.Person;
import nagarro.exception.DatabaseOperationException;
import nagarro.exception.PersonCreationException;
import nagarro.exception.PersonNotFoundException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToCreateStatementException;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.util.List;
import java.util.Optional;

public class PersonDAOImpl implements PersonDAO {

    private final Jdbi jdbi;

    public PersonDAOImpl(final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void createTable() {
        try {
            jdbi.useHandle(handle -> handle.execute("CREATE TABLE IF NOT EXISTS person (id SERIAL PRIMARY KEY, name VARCHAR, age INTEGER)"));
        } catch (UnableToExecuteStatementException | UnableToCreateStatementException e) {
            throw new DatabaseOperationException("Error creating table: " + e.getMessage(), e);
        }
    }

    @Override
    public int createPerson(final Person person) {
        if (person.getName() == null || person.getName().isEmpty() || person.getAge() < 0) {
            throw new PersonCreationException("Invalid person data: " + person);
        }
        try {
            return jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO person (name, age) VALUES (:name, :age)").bindBean(person).executeAndReturnGeneratedKeys("id").mapTo(int.class).one());
        } catch (RuntimeException e) {
            throw new PersonCreationException("Error creating person: " + person, e);
        }
    }

    @Override
    public List<Person> getAllPersons() {
        try {
            return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM person").map(new PersonMapper()).list());
        } catch (RuntimeException e) {
            throw new DatabaseOperationException("Error retrieving all persons: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Person> getPersonById(final int id) {
        try {
            return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM person WHERE id = :id").bind("id", id).map(new PersonMapper()).findFirst());
        } catch (UnableToExecuteStatementException | UnableToCreateStatementException e) {
            throw new PersonNotFoundException("Person not found with id: " + id);
        }
    }

    @Override
    public void updatePerson(final Person person) {
        try {
            final var retrievedPerson = getPersonById(person.getId());
            if (retrievedPerson.isEmpty()) {
                throw new PersonNotFoundException("Person not found with id: " + person.getId());
            }
            jdbi.useHandle(handle -> handle.createUpdate("UPDATE person SET name = :name, age = :age WHERE id = :id").bindBean(person).execute());

        } catch (RuntimeException e) {
            throw new DatabaseOperationException("Error updating person: " + person, e);
        }
    }

    @Override
    public void deletePerson(final int id) {
        try {
            final int personCount = jdbi.withHandle(handle -> handle.createQuery("SELECT COUNT(*) FROM person WHERE id = :id").bind("id", id).mapTo(Integer.class).one());
            if (personCount == 0) {
                throw new PersonNotFoundException("Person not found with id: " + id);
            }
            jdbi.useHandle(handle -> handle.createUpdate("DELETE FROM person WHERE id = :id").bind("id", id).execute());
        } catch (RuntimeException e) {
            throw new DatabaseOperationException("Error deleting person: " + id, e);
        }
    }
}