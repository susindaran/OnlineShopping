package com.betadevels.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.CardDetail;
import org.hibernate.SessionFactory;

public class CardDetailHibernateDAO extends BaseHibernateDAO<CardDetail> implements GenericDAO<CardDetail>
{
    @Inject
    public CardDetailHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public Optional<CardDetail> findByCardNumber(String cardNumber)
    {
        if( Strings.isNullOrEmpty( cardNumber ) )
        {
            return Optional.absent();
        }
        else
        {
            return Optional.fromNullable(get(cardNumber));
        }
    }

    public void deleteByCardNumber(String cardNumber)
    {
        Optional<CardDetail> cardDetail = Optional.fromNullable(get(cardNumber));
        if( !cardDetail.isPresent() )
        {
            throw new NotFoundException("Unable to find the given Card Number");
        }
        else
        {
            delete( cardDetail.get() );
        }
    }
}
