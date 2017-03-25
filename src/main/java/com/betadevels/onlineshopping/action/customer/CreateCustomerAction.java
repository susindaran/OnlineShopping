package com.betadevels.onlineshopping.action.customer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.betadevels.onlineshopping.exceptions.ConflictingRequestException;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.payload.request.customer.CreateCustomerRequest;
import com.betadevels.onlineshopping.payload.response.customer.CustomerResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.betadevels.onlineshopping.util.PasswordEncipher;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

@Slf4j
public class CreateCustomerAction implements Action<CustomerResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private CreateCustomerRequest createCustomerRequest;
    private boolean isAdmin;

    @Inject
    public CreateCustomerAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public CreateCustomerAction withRequest(CreateCustomerRequest createCustomerRequest)
    {
        this.createCustomerRequest = createCustomerRequest;
        return this;
    }

    public CreateCustomerAction isAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
        return this;
    }

    @Override
    public CustomerResponse invoke()
    {
        CustomerHibernateDAO customerHibernateDAO = hibernateUtil.getCustomerHibernateDAO();
        try
        {
            Customer customer = Customer.builder()
                    .firstName(createCustomerRequest.getFirstName())
                    .lastName(createCustomerRequest.getLastName())
                    .emailId(createCustomerRequest.getEmailId())
                    .password(PasswordEncipher.encryptWithMD5(createCustomerRequest.getPassword()))
                    .isAdmin(isAdmin)
                    .build();
            Customer newCustomer = customerHibernateDAO.create(customer);
            return modelMapper.map(newCustomer, CustomerResponse.class);
        }
        catch (HibernateException e)
        {
            log.error(String.valueOf(e.getCause()));
            String errorMessage = e.getCause().getMessage();
            if( errorMessage.contains("Duplicate entry") )
                throw new ConflictingRequestException("Email ID already exists");
            throw new InternalErrorException(e.getMessage());
        }
    }
}
