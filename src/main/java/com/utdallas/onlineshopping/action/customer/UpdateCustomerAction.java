package com.utdallas.onlineshopping.action.customer;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.exceptions.ConflictingRequestException;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.payload.request.customer.UpdateCustomerRequest;
import com.utdallas.onlineshopping.payload.response.customer.CustomerResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import com.utdallas.onlineshopping.util.PasswordEncipher;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

@Slf4j
public class UpdateCustomerAction implements Action<CustomerResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private UpdateCustomerRequest updateCustomerRequest;
    private Long customer_id;

    public UpdateCustomerAction withRequest(UpdateCustomerRequest updateCustomerRequest)
    {
        this.updateCustomerRequest = updateCustomerRequest;
        return this;
    }

    public UpdateCustomerAction withId(Long id)
    {
        this.customer_id = id;
        return this;
    }

    @Inject
    public UpdateCustomerAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerResponse invoke()
    {
        CustomerHibernateDAO customerHibernateDAO = hibernateUtil.getCustomerHibernateDAO();
        Customer customer = customerHibernateDAO.findById( customer_id ).get();

        try
        {
            if( !Strings.isNullOrEmpty(updateCustomerRequest.getFirstName()) )
                customer.setFirstName(updateCustomerRequest.getFirstName());
            if( !Strings.isNullOrEmpty(updateCustomerRequest.getLastName()) )
                customer.setLastName(updateCustomerRequest.getLastName());
            if( !Strings.isNullOrEmpty(updateCustomerRequest.getEmailId()) )
                customer.setEmailId(updateCustomerRequest.getEmailId());
            if( !Strings.isNullOrEmpty(updateCustomerRequest.getPassword()) )
                customer.setPassword(PasswordEncipher.encryptWithMD5(updateCustomerRequest.getPassword()));
            Customer newCustomer = customerHibernateDAO.update(customer);
            return modelMapper.map(newCustomer, CustomerResponse.class);
        }
        catch( HibernateException e )
        {
            log.error(String.valueOf(e.getCause()));
            String errorMessage = e.getCause().getMessage();
            if( errorMessage.contains("Duplicate entry") )
                throw new ConflictingRequestException("Email ID already exists");
            throw new InternalErrorException(e.getMessage());
        }
    }
}
