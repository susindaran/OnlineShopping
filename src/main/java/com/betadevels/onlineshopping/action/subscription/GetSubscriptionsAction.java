package com.betadevels.onlineshopping.action.subscription;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.SubscriptionHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.models.Subscription;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionListResponse;
import com.betadevels.onlineshopping.payload.response.subscription.SubscriptionResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.betadevels.onlineshopping.util.Utility.preparePaginationLinks;

/**
 * Created by prathyusha on 4/6/17.
 */
public class GetSubscriptionsAction implements Action<SubscriptionListResponse> {
        private HibernateUtil hibernateUtil;
        private ModelMapper modelMapper;
        private String requestURL;
        private int page, size;
        private Long customerId;

        @Inject
        public GetSubscriptionsAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
        {
            this.hibernateUtil = hibernateUtilProvider.get();
            this.modelMapper = modelMapper;
        }


        public GetSubscriptionsAction forCustomerId(Long customerId )
        {
            this.customerId = customerId;
            return this;
        }


        @Override
        public SubscriptionListResponse invoke()
        {
            //TODO: Validate pagination parameters
            SubscriptionHibernateDAO subscriptionHibernateDAO = this.hibernateUtil.getSubscriptionHibernateDAO();
            CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();

            Optional<Customer> customerOptional = customerHibernateDAO.findById(customerId);
            if( customerOptional.isPresent() )
            {
                Customer customer = customerOptional.get();
                List<Subscription> subscriptions = subscriptionHibernateDAO.getSubscriptionsOfUser(page, size, customer );
                List<SubscriptionResponse> subscriptionResponses = subscriptions.stream().map(subscription -> modelMapper.map(subscription, SubscriptionResponse.class)).collect(Collectors.toList());

                return SubscriptionListResponse.builder()
                        .subscriptions( subscriptionResponses )
                        .build();
            }
            throw new NotFoundException(Collections.singletonList("No customer matching the given customer_id"));
        }
    }



