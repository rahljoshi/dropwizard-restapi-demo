package nagarro;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import nagarro.dao.PersonDAO;
import nagarro.dao.mapper.PersonMapper;
import nagarro.exception.mapper.GeneralExceptionMapper;
import nagarro.resources.PersonResource;
import nagarro.service.impl.PersonServiceImpl;

public class DemoApplication extends Application<DemoConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "Demo";
    }

    /**
     * @param bootstrap the application bootstrap
     */
    @Override
    public void initialize(final Bootstrap<DemoConfiguration> bootstrap) {
    }

    @Override
    public void run(DemoConfiguration configuration, Environment environment) {
        final var factory = new JdbiFactory();
        final var jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        jdbi.registerRowMapper(new PersonMapper());
        final var personDAO = jdbi.onDemand(PersonDAO.class);
        personDAO.createTable();
        final var personService = new PersonServiceImpl(personDAO);
        final var personResource = new PersonResource(personService);
        environment.jersey().register(new GeneralExceptionMapper());
        environment.jersey().register(personResource);
    }
}
