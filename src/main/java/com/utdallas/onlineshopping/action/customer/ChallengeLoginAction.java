package com.utdallas.onlineshopping.action.customer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.exceptions.AuthorizationFailedException;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.payload.request.customer.ChallengeLoginRequest;
import com.utdallas.onlineshopping.payload.response.customer.ChallengeLoginResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import com.utdallas.onlineshopping.util.PasswordEncipher;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ChallengeLoginAction implements Action<ChallengeLoginResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private ChallengeLoginRequest challengeLoginRequest;

    @Inject
    public ChallengeLoginAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public ChallengeLoginAction withRequest(ChallengeLoginRequest challengeLoginRequest)
    {
        this.challengeLoginRequest = challengeLoginRequest;
        return this;
    }

    @Override
    public ChallengeLoginResponse invoke()
    {
        CustomerHibernateDAO customerHibernateDAO = hibernateUtil.getCustomerHibernateDAO();
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailId", challengeLoginRequest.getEmailId());
        queryParams.put("password", PasswordEncipher.encryptWithMD5(challengeLoginRequest.getPassword()));

        try
        {
            List<Customer> customerList = customerHibernateDAO.findByParams(queryParams);

            //customerList cannot be greater than 1, still having the check here
            //so that even if the customer creation was changed in future, this should
            //prevent duplicate entries of Email ID in the database.
            if( !customerList.isEmpty() && customerList.size() == 1)
            {
                Customer customer = customerList.get(0);
                return ChallengeLoginResponse.builder()
                        .customerId(customer.getCustomerId())
                        .emailId(customer.getEmailId())
                        .message("Login successful!")
                        .build();
            }
        }
        catch (HibernateException e)
        {
            log.error(String.valueOf(e.getCause()));
            throw new InternalErrorException(e.getCause().getMessage());
        }
        throw new AuthorizationFailedException("Invalid username or password");
    }
}
