package com.utdallas.onlineshopping;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.utdallas.onlineshopping.configurations.OnlineShoppingConfiguration;
import com.utdallas.onlineshopping.health.OnlineShoppingHealthCheck;
import com.utdallas.onlineshopping.inject.HibernateModule;
import com.utdallas.onlineshopping.inject.OnlineShoppingModule;
import com.utdallas.onlineshopping.models.Address;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.models.TaxDetails;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

@Slf4j
public class OnlineShoppingApplication extends Application<OnlineShoppingConfiguration>
{
    @Override
    public String getName() {
        return "OnlineShopping";
    }

    private static final HibernateBundle<OnlineShoppingConfiguration> HIBERNATE_BUNDLE =
            new HibernateBundle<OnlineShoppingConfiguration>(
                    Customer.class,
                    TaxDetails.class,
                    Address.class
            )
            {
                @Override
                public DataSourceFactory getDataSourceFactory(OnlineShoppingConfiguration onlineShoppingConfiguration)
                {
                    return onlineShoppingConfiguration.getDatabaseConfiguration();
                }

                @Override
                protected void configure(Configuration configuration)
                {
                    configuration.setNamingStrategy(new CustomNamingStrategy());
                }
            };

    private static final GuiceBundle<OnlineShoppingConfiguration> GUICE_BUNDLE =
            GuiceBundle.<OnlineShoppingConfiguration>newBuilder()
                    .addModule(new HibernateModule(HIBERNATE_BUNDLE))
                    .addModule(new OnlineShoppingModule())
                    .setConfigClass(OnlineShoppingConfiguration.class)
                    .enableAutoConfig("com.utdallas.onlineshopping")
                    .build(Stage.DEVELOPMENT);

    public static GuiceBundle<OnlineShoppingConfiguration> getGuiceBundle()
    {
        return GUICE_BUNDLE;
    }

    @Override
    public void initialize(Bootstrap<OnlineShoppingConfiguration> bootstrap)
    {
        DateTimeZone.setDefault(DateTimeZone.forID("America/Chicago"));
        TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));

        bootstrap.addBundle(HIBERNATE_BUNDLE);
        bootstrap.addBundle(GUICE_BUNDLE);

    }

    @Override
    public void run(OnlineShoppingConfiguration onlineShoppingConfiguration, Environment environment) throws Exception
    {
        log.info("ONLINE SHOPPING BACKEND SERVICE STARTED!");
        environment.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        environment.healthChecks().register("Online Shopping Health Check", new OnlineShoppingHealthCheck());
    }

    public static void main(String[] args) throws Exception
    {
        new OnlineShoppingApplication().run(args);
    }
}
