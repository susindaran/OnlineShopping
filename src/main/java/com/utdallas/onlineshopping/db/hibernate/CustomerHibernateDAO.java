package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Customer;
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

    public void reloadCustomer( Customer customer )
    {
        currentSession().flush();
        currentSession().refresh( customer );
    }
}
