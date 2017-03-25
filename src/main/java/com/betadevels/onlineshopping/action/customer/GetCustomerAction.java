package com.betadevels.onlineshopping.action.customer;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.payload.response.customer.CustomerResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

public class GetCustomerAction implements Action<CustomerResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private Long id;

    @Inject
    public GetCustomerAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public GetCustomerAction withId( Long id )
    {
        this.id = id;
        return this;
    }

    public CustomerResponse invoke()
    {
        Optional<Customer> customerOptional = hibernateUtil.getCustomerHibernateDAO().findById(id);
        if( customerOptional.isPresent() )
        {
            return modelMapper.map(customerOptional.get(), CustomerResponse.class);
        }
        else
        {
            throw new NotFoundException("No customer matching the given ID");
        }
    }
}
