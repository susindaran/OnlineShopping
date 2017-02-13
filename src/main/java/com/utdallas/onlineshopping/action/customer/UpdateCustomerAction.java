package com.utdallas.onlineshopping.action.customer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.payload.request.customer.UpdateCustomerRequest;
import com.utdallas.onlineshopping.payload.response.customer.CustomerResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import com.utdallas.onlineshopping.util.PasswordEncrypter;
import org.modelmapper.ModelMapper;

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
        if( updateCustomerRequest.getFirstName() != null )
            customer.setFirstName(updateCustomerRequest.getFirstName());
        if( updateCustomerRequest.getLastName() != null )
            customer.setLastName(updateCustomerRequest.getLastName());
        if( updateCustomerRequest.getEmailId() != null)
            customer.setEmailId(updateCustomerRequest.getEmailId());
        if( updateCustomerRequest.getPassword() != null )
            customer.setPassword(PasswordEncrypter.encryptWithMD5(updateCustomerRequest.getPassword()));

        customerHibernateDAO.update(customer);
        return modelMapper.map(customer, CustomerResponse.class);
    }
}
