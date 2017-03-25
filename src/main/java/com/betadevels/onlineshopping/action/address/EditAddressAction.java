package com.betadevels.onlineshopping.action.address;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.AddressHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Address;
import com.betadevels.onlineshopping.payload.request.address.AddressRequest;
import com.betadevels.onlineshopping.payload.response.address.AddressResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

public class EditAddressAction implements Action<AddressResponse>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private Long addressId;
    private AddressRequest addressRequest;

    @Inject
    public EditAddressAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public EditAddressAction forAddressId( Long addressId )
    {
        this.addressId = addressId;
        return this;
    }

    public EditAddressAction withRequest( AddressRequest addressRequest )
    {
        this.addressRequest = addressRequest;
        return this;
    }

    @Override
    public AddressResponse invoke()
    {
        AddressHibernateDAO addressHibernateDAO = this.hibernateUtil.getAddressHibernateDAO();
        Optional<Address> addressOptional = addressHibernateDAO.findById(addressId);
        if( addressOptional.isPresent() )
        {
            Address address = addressOptional.get();

            if( !Strings.isNullOrEmpty( addressRequest.getPhone() ) )
                address.setPhone( addressRequest.getPhone() );
            if( !Strings.isNullOrEmpty( addressRequest.getStreet() ) )
                address.setStreet( addressRequest.getStreet() );
            if( !Strings.isNullOrEmpty( addressRequest.getCity() ) )
                address.setCity(  addressRequest.getCity());
            if( !Strings.isNullOrEmpty( addressRequest.getState() ) )
                address.setTaxDetails( hibernateUtil.getTaxDetailsHibernateDAO().findByState( addressRequest.getState() ) );
            if( !Strings.isNullOrEmpty( addressRequest.getCountry() ) )
                address.setCountry( addressRequest.getCountry() );
            if( !Strings.isNullOrEmpty( addressRequest.getZipcode() ) )
                address.setZipcode( addressRequest.getZipcode() );
            if( !Strings.isNullOrEmpty( addressRequest.getType() ) )
                address.setType( addressRequest.getType() );
            if( !Strings.isNullOrEmpty( addressRequest.getName() ) )
                address.setName( addressRequest.getName() );

            Address newAddress = addressHibernateDAO.update(address);

            return modelMapper.map( newAddress, AddressResponse.class );
        }
        else
        {
            throw new NotFoundException("No Address matching the given ID");
        }
    }
}
