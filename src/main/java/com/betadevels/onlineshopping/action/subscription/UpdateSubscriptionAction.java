package com.betadevels.onlineshopping.action.subscription;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.SubscriptionHibernateDAO;
import com.betadevels.onlineshopping.enumerations.SubscriptionStatus;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Subscription;
import com.betadevels.onlineshopping.payload.request.subscription.UpdateSubscriptionRequest;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import java.util.Collections;

@Slf4j
public class UpdateSubscriptionAction implements Action<SubscriptionResponse>
{
        private final HibernateUtil hibernateUtil;
        private ModelMapper modelMapper;
        private UpdateSubscriptionRequest updateSubscriptionRequest;
        private Long subscriptionId;

        @Inject
        public UpdateSubscriptionAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
        {
            this.hibernateUtil = hibernateUtilProvider.get();
            this.modelMapper = modelMapper;
        }

        public UpdateSubscriptionAction withRequest(UpdateSubscriptionRequest updateSubscriptionRequest)
        {
            this.updateSubscriptionRequest = updateSubscriptionRequest;
            return this;
         }

        public UpdateSubscriptionAction withId(Long subscriptionId)
        {
            this.subscriptionId = subscriptionId;
            return this;
        }

        @Override
        public SubscriptionResponse invoke() {

            SubscriptionHibernateDAO subscriptionHibernateDAO = this.hibernateUtil.getSubscriptionHibernateDAO();
            Optional<Subscription> subscriptionOptional=subscriptionHibernateDAO.findById(subscriptionId);
            if( !subscriptionOptional.isPresent() )
            {
                throw new NotFoundException( Collections.singletonList( "No Subscription matching the given subscription_id" ) );
            }

            Subscription currentSubscription = subscriptionOptional.get();

            try
            {
                if( updateSubscriptionRequest.getFrequencyInDays() != null && updateSubscriptionRequest.getFrequencyInDays() > 0 )
                {
                    currentSubscription.setFrequencyInDays( updateSubscriptionRequest.getFrequencyInDays() );
                }
                if ( updateSubscriptionRequest.getQuantity() != null && updateSubscriptionRequest.getQuantity() > 0 )
                {
                    currentSubscription.setQuantity( updateSubscriptionRequest.getQuantity() );
                }
                if(updateSubscriptionRequest.getStatus()== SubscriptionStatus.INACTIVE)
                {
                    currentSubscription.setStatus(updateSubscriptionRequest.getStatus().getStatus());
                }
                if(updateSubscriptionRequest.getStatus()== SubscriptionStatus.ACTIVE)
                {
                    currentSubscription.setStatus(updateSubscriptionRequest.getStatus().getStatus());
                }
                Subscription newSubscription = subscriptionHibernateDAO.update(currentSubscription);
                return modelMapper.map(newSubscription, SubscriptionResponse.class);
            }
            catch (HibernateException e)
            {
                log.error(String.valueOf(e.getCause()));
                throw new InternalErrorException(e.getMessage());
            }

        }
    }


