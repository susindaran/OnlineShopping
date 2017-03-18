package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Address;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class AddressHibernateDAO extends BaseHibernateDAO<Address> implements GenericDAO<Address>
{
    @Inject
    public AddressHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public void deleteById(Long addressId)
    {
        Optional<Address> address = findById(addressId);
        if( !address.isPresent() )
        {
            throw new NotFoundException("Unable to find the given Address ID");
        }
        else
        {
            delete( address.get());
        }
    }

    public Optional<Address> findByIdForCustomer( Long addressId, Long customerId )
    {
        Object result = criteria().add(Restrictions.eq("addressId", addressId)).add(Restrictions.eq("customer.customerId", customerId)).uniqueResult();
        return Optional.fromNullable( (Address)result );
    }
}
