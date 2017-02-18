package com.utdallas.onlineshopping.action.address;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.AddressHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.TaxDetailsHibernateDAO;
import com.utdallas.onlineshopping.models.Address;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.models.TaxDetails;
import com.utdallas.onlineshopping.payload.request.address.AddAddressRequest;
import com.utdallas.onlineshopping.payload.response.address.AddressResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.Collections;

/**
 * Created by susindaran on 2/16/17.
 */
public class AddAddressAction implements Action<AddressResponse>
{

    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private AddAddressRequest addAddressRequest;
    private Long customerId;

    @Inject
    public AddAddressAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public AddAddressAction withRequest(AddAddressRequest addAddressRequest)
    {
        this.addAddressRequest = addAddressRequest;
        return this;
    }

    public AddAddressAction forCustomerId(Long customerId)
    {
        this.customerId = customerId;
        return this;
    }

    @Override
    public AddressResponse invoke()
    {
        TaxDetailsHibernateDAO taxDetailsHibernateDAO = this.hibernateUtil.getTaxDetailsHibernateDAO();
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();
        AddressHibernateDAO addressHibernateDAO = this.hibernateUtil.getAddressHibernateDAO();

        //TODO: Make validations on the state and customerId and throw exceptions accordingly
        TaxDetails taxDetails = taxDetailsHibernateDAO.findByState(addAddressRequest.getState());
        Customer customer = customerHibernateDAO.findById(this.customerId).get();

        Address address = modelMapper.map(addAddressRequest, Address.class);
        address.setTaxDetails(taxDetails);
        address.setCustomer(customer);

        Address newAddress = addressHibernateDAO.create(address);

        AddressResponse addressResponse = modelMapper.map(newAddress, AddressResponse.class);
        addressResponse.setCustomerId(customer.getCustomerId());

        return addressResponse;
    }
}
