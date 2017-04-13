package com.betadevels.onlineshopping.action.subscription;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.SubscriptionHibernateDAO;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.models.Subscription;
import com.betadevels.onlineshopping.payload.request.subscription.UpdateSubscriptionRequest;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.joda.time.LocalDateTime;
import org.modelmapper.ModelMapper;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by prathyusha on 4/13/17.
 */
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
            Subscription currentSubscription=subscriptionHibernateDAO.getSubscriptionById(subscriptionId);
            try {
                if (updateSubscriptionRequest.getFrequencyInDays() != null)
                    currentSubscription.setFrequencyInDays(updateSubscriptionRequest.getFrequencyInDays());
                if (updateSubscriptionRequest.getQuantity() != null)
                    currentSubscription.setQuantity(updateSubscriptionRequest.getQuantity());

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


