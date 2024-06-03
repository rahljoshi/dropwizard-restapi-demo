package nagarro.dao;

import nagarro.dao.mapper.PersonMapper;
import nagarro.entity.Person;
import nagarro.exception.PersonNotFoundException;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(PersonMapper.class)
public interface PersonDAO {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS person (id SERIAL PRIMARY KEY, name VARCHAR, age INTEGER)")
    void createTable();

    @SqlUpdate("INSERT INTO person (name, age) VALUES (:name, :age)")
    @GetGeneratedKeys
    int createPerson(@BindBean Person person);

    @SqlQuery("SELECT * FROM person")
    List<Person> getAllPersons();

    @SqlQuery("SELECT * FROM person WHERE id = :id")
    Optional<Person> getPersonById(@Bind("id") int id) throws PersonNotFoundException;

    @SqlUpdate("UPDATE person SET name = :name, age = :age WHERE id = :id")
    void updatePerson(@BindBean Person person);

    @SqlUpdate("DELETE FROM person WHERE id = :id")
    void deletePerson(@Bind("id") int id) throws PersonNotFoundException;
}
