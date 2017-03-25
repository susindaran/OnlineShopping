package com.betadevels.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.models.TaxDetails;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

import java.util.Collections;
import java.util.List;

public class TaxDetailsHibernateDAO extends BaseHibernateDAO<TaxDetails> implements GenericDAO<TaxDetails>
{
    @Inject
    public TaxDetailsHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public TaxDetails findByState(String state)
    {
        return findByParams(Collections.singletonMap("state", state)).get(0);
    }

    public List<String> getAllStates()
    {
        Criteria criteria = currentSession().createCriteria( TaxDetails.class ).setProjection( Projections.property( "state" ) );
        return criteria.list();
    }
}
