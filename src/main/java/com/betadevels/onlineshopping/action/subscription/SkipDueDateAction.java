package com.betadevels.onlineshopping.action.subscription;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.SubscriptionHibernateDAO;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Subscription;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import org.joda.time.LocalDateTime;

import com.google.common.base.Optional;

import java.util.Collections;

@Slf4j
public class SkipDueDateAction implements Action<SubscriptionResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private Long subscriptionId;

    @Inject
    public SkipDueDateAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public SkipDueDateAction forSubscriptionId( Long subscriptionId )
    {
        this.subscriptionId = subscriptionId;
        return this;
    }

    @Override
    public SubscriptionResponse invoke()
    {
        SubscriptionHibernateDAO subscriptionHibernateDAO = this.hibernateUtil.getSubscriptionHibernateDAO();

        Optional<Subscription> subscriptionOptional = subscriptionHibernateDAO.findById( subscriptionId );
        if( !subscriptionOptional.isPresent() )
        {
            throw new NotFoundException( Collections.singletonList( "No subscription matching the given subscription ID" ) );
        }

        Subscription subscription = subscriptionOptional.get();
        LocalDateTime nextDueDate = subscription.getNextDueDate().plusDays( subscription.getFrequencyInDays() );
        subscription.setNextDueDate( nextDueDate );

        try
        {
            Subscription updatedSubscription = subscriptionHibernateDAO.update( subscription );
            return modelMapper.map( updatedSubscription, SubscriptionResponse.class );
        }
        catch ( HibernateException e )
        {
            log.error( e.getCause().getMessage() );
            throw new InternalErrorException( Collections.singletonList( e.getCause().getMessage() ) );
        }
    }
}
