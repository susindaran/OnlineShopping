package com.utdallas.onlineshopping.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.utdallas.onlineshopping.configurations.OnlineShoppingConfiguration;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.db.hibernate.AddressHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.CardDetailHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.TaxDetailsHibernateDAO;
import com.utdallas.onlineshopping.models.Address;
import com.utdallas.onlineshopping.models.CardDetail;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.models.TaxDetails;
import io.dropwizard.hibernate.HibernateBundle;

public class HibernateModule extends AbstractModule
{
    public static final Class[] ENTITIES = new Class[]
            {
                    Customer.class,
                    TaxDetails.class,
                    Address.class,
                    CardDetail.class
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
        bind(new TypeLiteral<GenericDAO<TaxDetails>>(){}).to(TaxDetailsHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<Address>>(){}).to(AddressHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<CardDetail>>(){}).to(CardDetailHibernateDAO.class);
    }

    @Provides
    public CustomerHibernateDAO provideCustomerHibernateDAO()
    {
        return new CustomerHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public TaxDetailsHibernateDAO provideTaxDetailsHibernateDAO()
    {
        return new TaxDetailsHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public AddressHibernateDAO provideAddressHibernateDAO()
    {
        return new AddressHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public CardDetailHibernateDAO provideCardDetailHibernateDAO()
    {
        return new CardDetailHibernateDAO(hibernateBundle.getSessionFactory());
    }
}
