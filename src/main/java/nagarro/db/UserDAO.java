package nagarro.db;

import io.dropwizard.hibernate.AbstractDAO;
import nagarro.core.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<User> {

    public UserDAO(SessionFactory factory) {
        super(factory);
    }

    public User create(User user) {
        return persist(user);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public List<User> findAll() {
        return list((Query<User>) namedQuery("User.findAll"));
    }

    public User update(User user) {
        return persist(user);
    }

    public void delete(User user) {
        currentSession().delete(user);
    }
}
