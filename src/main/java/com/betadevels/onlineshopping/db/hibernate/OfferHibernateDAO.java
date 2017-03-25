package com.betadevels.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.models.Offer;
import org.hibernate.SessionFactory;

public class OfferHibernateDAO extends BaseHibernateDAO<Offer> implements GenericDAO<Offer>
{
    @Inject
    public OfferHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }
}
