package com.betadevels.onlineshopping.action.subscription;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.SubscriptionHibernateDAO;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Subscription;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import java.util.Collections;

public class DeleteSubscriptionAction implements Action<Void>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private Long subscriptionId;

    @Inject
    public DeleteSubscriptionAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public DeleteSubscriptionAction withSubscriptionId(Long subscriptionId)
    {
        this.subscriptionId= subscriptionId;
        return this;
    }

    @Override
    public Void invoke()
    {
        SubscriptionHibernateDAO subscriptionHibernateDAO = this.hibernateUtil.getSubscriptionHibernateDAO();
        Optional<Subscription> subscriptionOptional = subscriptionHibernateDAO.findById(subscriptionId);
        try
        {
            if(!subscriptionOptional.isPresent())
            {
                throw new NotFoundException( Collections.singletonList("No subscription matching the given subscription_id") );
            }
            Subscription subscription = subscriptionOptional.get();
            subscriptionHibernateDAO.delete(subscription);
        }
        catch (HibernateException e)
        {
            throw new InternalErrorException(e.getCause().getMessage());
        }
        return null;
    }
}



