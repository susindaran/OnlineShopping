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

    public Customer create(Customer customer)
    {
        return persist(customer);
    }

    public Optional<Customer> findById(Long id)
    {
        if( id != null )
        {
            return Optional.fromNullable(get(id));
        }
        else
        {
            return Optional.absent();
        }
    }

    public Customer update(Customer customer)
    {
        Customer newCustomer = persist(customer);
        //Flushing the session explicitly here because hibernate sometimes decides not to
        //update the entity immediately, which causes the exception not to be thrown until
        //the transaction is committed.
        this.currentSession().flush();
        return newCustomer;
    }

    @Override
    public void delete(Customer customer)
    {
        currentSession().delete( customer );
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

    public Customer merge(Customer obj)
    {
        return null;
    }
}
