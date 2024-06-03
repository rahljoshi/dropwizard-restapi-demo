package nagarro.dao.mapper;

import nagarro.entity.Person;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {
    @Override
    public Person map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Person(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age")
        );
    }
}