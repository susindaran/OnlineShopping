package com.utdallas.onlineshopping.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.utdallas.onlineshopping.configurations.OnlineShoppingConfiguration;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.models.Customer;
import io.dropwizard.hibernate.HibernateBundle;

public class HibernateModule extends AbstractModule
{
    public static final Class[] ENTITIES = new Class[]
            {
                    Customer.class
            };

    private final HibernateBundle<OnlineShoppingConfiguration> hibernateBundle;

    public HibernateModule(HibernateBundle<OnlineShoppingConfiguration> hibernateBundle)
    {
        this.hibernateBundle = hibernateBundle;
    }

    @Override
    protected void configure()
    {
        bind(new TypeLiteral<GenericDAO<Customer>>(){}).to(CustomerHibernateDAO.class);
    }

    @Provides
    public CustomerHibernateDAO provideCustomerHibernateDAO()
    {
        return new CustomerHibernateDAO(hibernateBundle.getSessionFactory());
    }
}
