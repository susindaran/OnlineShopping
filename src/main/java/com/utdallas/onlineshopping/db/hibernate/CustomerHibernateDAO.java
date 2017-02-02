package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
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

    public Customer update(Customer obj)
    {
        return null;
    }

    public void delete(Customer customer)
    {

    }

    public Customer merge(Customer obj)
    {
        return null;
    }
}
