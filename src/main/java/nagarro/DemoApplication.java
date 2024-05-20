package nagarro;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import nagarro.core.User;
import nagarro.db.UserDAO;
import nagarro.health.ApplicationHealthCheck;
import nagarro.health.DatabaseHealthCheck;
import nagarro.resources.PersonResource;
import nagarro.resources.UserResource;

public class DemoApplication extends Application<DemoConfiguration> {

    private final HibernateBundle<DemoConfiguration> hibernateBundle =
            new HibernateBundle<DemoConfiguration>(User.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(DemoConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    public static void main(final String[] args) throws Exception {
        new DemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "Demo";
    }

    @Override
    public void initialize(final Bootstrap<DemoConfiguration> bootstrap) {
        // TODO: application initialization
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final DemoConfiguration configuration,
                    final Environment environment) {
        final UserDAO userDAO = new UserDAO(hibernateBundle.getSessionFactory());
        environment.jersey().register(new UserResource(userDAO));
        environment.jersey().register(new PersonResource());
        environment
                .healthChecks()
                .register("application", new ApplicationHealthCheck());
        final DatabaseHealthCheck healthCheck = new DatabaseHealthCheck(hibernateBundle.getSessionFactory());
        environment.healthChecks().register("database", healthCheck);
    }

}
