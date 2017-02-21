package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.CardDetail;
import org.hibernate.SessionFactory;

public class CardDetailHibernateDAO extends BaseHibernateDAO<CardDetail> implements GenericDAO<CardDetail>
{
    @Inject
    public CardDetailHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Override
    public CardDetail create(CardDetail cardDetail)
    {
        return persist(cardDetail);
    }

    @Override
    public Optional<CardDetail> findById(Long id) {
        return null;
    }

    @Override
    public CardDetail update(CardDetail obj) {
        return null;
    }

    @Override
    public void delete(CardDetail obj) {

    }

    @Override
    public CardDetail merge(CardDetail obj) {
        return null;
    }
}
