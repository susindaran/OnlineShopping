package com.betadevels.onlineshopping.action.address;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.AddressHibernateDAO;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

public class DeleteAddressAction implements Action<Void>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private Long addressId;

    @Inject
    public DeleteAddressAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public DeleteAddressAction withAddressId( Long addressId )
    {
        this.addressId = addressId;
        return this;
    }

    @Override
    public Void invoke()
    {
        AddressHibernateDAO addressHibernateDAO = hibernateUtil.getAddressHibernateDAO();
        try
        {
            addressHibernateDAO.deleteById(addressId);
        }
        catch (HibernateException e)
        {
            throw new InternalErrorException(e.getCause().getMessage());
        }
        return null;
    }
}
