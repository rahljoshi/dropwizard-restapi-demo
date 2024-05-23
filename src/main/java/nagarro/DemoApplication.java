package nagarro;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import nagarro.db.PersonDAO;
import nagarro.resources.PersonResource;
import nagarro.util.PersonMapper;
import org.jdbi.v3.core.Jdbi;

public class DemoApplication extends Application<DemoConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "Demo";
    }

    @Override
    public void initialize(final Bootstrap<DemoConfiguration> bootstrap) {
    }

    @Override
    public void run(DemoConfiguration configuration, Environment environment) {
        final var factory = new JdbiFactory();
        final var jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        jdbi.registerRowMapper(new PersonMapper());
        final var personDAO = jdbi.onDemand(PersonDAO.class);
        environment.jersey().register(new PersonResource(personDAO));
    }
}
