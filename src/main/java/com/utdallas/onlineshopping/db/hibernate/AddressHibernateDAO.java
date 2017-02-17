package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Address;
import org.hibernate.SessionFactory;

/**
 * Created by susindaran on 2/16/17.
 */
public class AddressHibernateDAO extends BaseHibernateDAO<Address> implements GenericDAO<Address>
{
    @Inject
    public AddressHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Override
    public Address create(Address address) {
        return persist(address);
    }

    @Override
    public Optional<Address> findById(Long id) {
        return null;
    }

    @Override
    public Address update(Address obj) {
        return null;
    }

    @Override
    public void delete(Address obj) {

    }

    @Override
    public Address merge(Address obj) {
        return null;
    }
}
