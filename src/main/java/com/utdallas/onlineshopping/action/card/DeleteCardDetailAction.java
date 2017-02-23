package com.utdallas.onlineshopping.action.card;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CardDetailHibernateDAO;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

public class DeleteCardDetailAction implements Action<Void>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String cardNumber;

    @Inject
    public DeleteCardDetailAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public DeleteCardDetailAction withCardNumber( String cardNumber )
    {
        this.cardNumber = cardNumber;
        return this;
    }

    @Override
    public Void invoke()
    {
        CardDetailHibernateDAO cardDetailHibernateDAO = this.hibernateUtil.getCardDetailHibernateDAO();

        try
        {
            cardDetailHibernateDAO.deleteByCardNumber( cardNumber );
        }
        catch (HibernateException e)
        {
            throw new InternalErrorException(e.getCause().getMessage());
        }

        return null;
    }
}
