package com.utdallas.onlineshopping.action.customer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

/**
 * Created by prathyusha on 2/24/17.
 */
public class DeleteCustomerAction implements Action<Void> {

    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private Long customerId;

    @Inject
    public DeleteCustomerAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public DeleteCustomerAction withCustomerId( Long customerId )
    {
        this.customerId = customerId;
        return this;
    }

    @Override
    public Void invoke() {
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();

        try
        {
            customerHibernateDAO.deleteByCustomerId( customerId );
        }
        catch (HibernateException e)
        {
            throw new InternalErrorException(e.getCause().getMessage());
        }
        return null;
    }
}
