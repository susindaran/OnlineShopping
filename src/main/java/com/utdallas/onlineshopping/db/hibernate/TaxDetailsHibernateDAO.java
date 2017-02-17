package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.TaxDetails;
import org.hibernate.SessionFactory;

import java.util.Collections;

/**
 * Created by susindaran on 2/16/17.
 */
public class TaxDetailsHibernateDAO extends BaseHibernateDAO<TaxDetails> implements GenericDAO<TaxDetails>
{
    @Inject
    public TaxDetailsHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Override
    public TaxDetails create(TaxDetails taxDetails) {
        return persist(taxDetails);
    }

    public TaxDetails findByState(String state)
    {
        return findByParams(Collections.singletonMap("state", state)).get(0);
    }

    @Override
    public Optional<TaxDetails> findById(Long id) {
        return null;
    }

    @Override
    public TaxDetails update(TaxDetails obj) {
        return null;
    }

    @Override
    public void delete(TaxDetails obj) {

    }

    @Override
    public TaxDetails merge(TaxDetails obj) {
        return null;
    }
}
