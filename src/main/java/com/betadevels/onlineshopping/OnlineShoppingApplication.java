package com.betadevels.onlineshopping;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.betadevels.onlineshopping.configurations.OnlineShoppingConfiguration;
import com.betadevels.onlineshopping.health.OnlineShoppingHealthCheck;
import com.betadevels.onlineshopping.inject.HibernateModule;
import com.betadevels.onlineshopping.inject.OnlineShoppingModule;
import com.betadevels.onlineshopping.models.*;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
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
                    Address.class,
                    CardDetail.class,
                    Product.class,
                    Category.class,
                    Order.class,
                    Shipment.class,
                    OrderDetail.class,
                    Cart.class,
                    Offer.class,
                    Payment.class,
                    Subscription.class
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
                    .enableAutoConfig("com.betadevels.onlineshopping")
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
        bootstrap.addBundle( new AssetsBundle( "/assets/", "/assets/" ) );
    }

    @Override
    public void run(OnlineShoppingConfiguration onlineShoppingConfiguration, Environment environment) throws Exception
    {
        log.info("ONLINE SHOPPING BACKEND SERVICE STARTED!");
        environment.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        environment.healthChecks().register("Online Shopping Health Check", new OnlineShoppingHealthCheck());
        environment.jersey().setUrlPattern( "/api/*" );
    }

    public static void main(String[] args) throws Exception
    {
        new OnlineShoppingApplication().run(args);
    }
}
