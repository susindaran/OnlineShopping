package com.betadevels.onlineshopping.action.subscription;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.SubscriptionHibernateDAO;
import com.betadevels.onlineshopping.enumerations.SubscriptionStatus;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.models.Subscription;
import com.betadevels.onlineshopping.payload.request.subscription.UpdateSubscriptionStatusRequest;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionListResponse;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by girijagodbole on 4/16/17.
 */
public class UpdateSubscriptionStatusAction implements Action<SubscriptionListResponse> {
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private UpdateSubscriptionStatusRequest updateSubscriptionStatusRequest;

    public UpdateSubscriptionStatusAction withRequest(UpdateSubscriptionStatusRequest updateShipmentsStatusRequest) {
        this.updateSubscriptionStatusRequest = updateShipmentsStatusRequest;
        return this;
    }

    @Inject
    public UpdateSubscriptionStatusAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper) {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public SubscriptionListResponse invoke() {
        SubscriptionHibernateDAO subscriptionHibernateDAO = hibernateUtil.getSubscriptionHibernateDAO();
        List<Subscription> subscriptionList = subscriptionHibernateDAO.getSubscriptionsByIds(updateSubscriptionStatusRequest.getSubscriptionIds());
        List<SubscriptionResponse> subscriptionResponses = new ArrayList<>();
        try {
            subscriptionList.forEach(subscription ->
            {
                if (updateSubscriptionStatusRequest.getStatus() == SubscriptionStatus.INACTIVE) {
                    subscription.setStatus(updateSubscriptionStatusRequest.getStatus().getStatus());
                } else if (updateSubscriptionStatusRequest.getStatus() == SubscriptionStatus.ACTIVE) {
                    subscription.setStatus(updateSubscriptionStatusRequest.getStatus().getStatus());
                }
                subscriptionResponses.add(modelMapper.map(subscriptionHibernateDAO.update(subscription), SubscriptionResponse.class));
            });
            SubscriptionListResponse subscriptionListResponse = new SubscriptionListResponse();
            subscriptionListResponse.setSubscriptions(subscriptionResponses );
            return subscriptionListResponse;

        }
        catch( HibernateException e )
        {
            e.printStackTrace();
            throw new InternalErrorException(e.getMessage());
        }
    }

}
