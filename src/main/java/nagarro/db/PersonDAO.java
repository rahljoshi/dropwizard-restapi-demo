package nagarro.db;

import nagarro.model.Person;
import nagarro.util.PersonMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterRowMapper(PersonMapper.class)
public interface PersonDAO  {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS person (id SERIAL PRIMARY KEY, name VARCHAR, age INTEGER)")
    void createTable();

    @SqlUpdate("INSERT INTO person (name, age) VALUES (:name, :age)")
    @GetGeneratedKeys
    int insertPerson(@Bind("name") String name, @Bind("age") int age);

    @SqlQuery("SELECT * FROM person")
    List<Person> getAllPersons();

    @SqlQuery("SELECT * FROM person WHERE id = :id")
    Person getPersonById(@Bind("id") int id);

    @SqlUpdate("UPDATE person SET name = :name, age = :age WHERE id = :id")
    void updatePerson(@Bind("id") int id, @Bind("name") String name, @Bind("age") int age);

    @SqlUpdate("DELETE FROM person WHERE id = :id")
    void deletePerson(@Bind("id") int id);
}
