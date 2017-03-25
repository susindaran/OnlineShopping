package com.betadevels.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Customer;
import org.hibernate.SessionFactory;

public class CustomerHibernateDAO extends BaseHibernateDAO<Customer> implements GenericDAO<Customer>
{
    @Inject
    public CustomerHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public void deleteByCustomerId(Long customerId)
    {
        Optional<Customer> customerOptional = Optional.fromNullable(get(customerId));
        if( !customerOptional.isPresent() )
        {
            throw new NotFoundException("Unable to find the given Card Number");
        }
        else
        {
            delete( customerOptional.get() );
        }
    }
}
