package com.utdallas.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Offer;
import org.hibernate.SessionFactory;

public class OfferHibernateDAO extends BaseHibernateDAO<Offer> implements GenericDAO<Offer>
{
    @Inject
    public OfferHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }
}
