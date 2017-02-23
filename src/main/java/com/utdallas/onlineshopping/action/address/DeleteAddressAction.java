package com.utdallas.onlineshopping.action.address;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.AddressHibernateDAO;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.util.HibernateUtil;
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
        catch (NotFoundException n)
        {
            throw n;
        }
        catch (HibernateException e)
        {
            throw new InternalErrorException(e.getCause().getMessage());
        }
        return null;
    }
}
