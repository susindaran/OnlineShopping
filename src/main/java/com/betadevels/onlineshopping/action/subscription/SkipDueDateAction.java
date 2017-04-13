package com.betadevels.onlineshopping.action.subscription;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.SubscriptionHibernateDAO;
import com.betadevels.onlineshopping.models.Subscription;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.modelmapper.ModelMapper;

import org.joda.time.LocalDateTime;


/**
 * Created by prathyusha on 4/13/17.
 */
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
    public SkipDueDateAction withId(Long subscriptionId)
    {
        this.subscriptionId = subscriptionId;
        return this;
    }

    @Override
    public SubscriptionResponse invoke() {

        SubscriptionHibernateDAO subscriptionHibernateDAO = this.hibernateUtil.getSubscriptionHibernateDAO();
        Subscription currentSubscription=subscriptionHibernateDAO.getSubscriptionById(subscriptionId);

        LocalDateTime newDate=currentSubscription.getNextDueDate().plusDays(currentSubscription.getFrequencyInDays());
        currentSubscription.setNextDueDate(newDate);


        return modelMapper.map(currentSubscription, SubscriptionResponse.class);

    }
}
