package com.utdallas.onlineshopping.action.customer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.exceptions.ConflictingRequestException;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.payload.request.customer.CreateCustomerRequest;
import com.utdallas.onlineshopping.payload.response.customer.CustomerResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import com.utdallas.onlineshopping.util.PasswordEncrypter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

@Slf4j
public class CreateCustomerAction implements Action<CustomerResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private CreateCustomerRequest createCustomerRequest;

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
                    .password(PasswordEncrypter.encryptWithMD5(createCustomerRequest.getPassword()))
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
