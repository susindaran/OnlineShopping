package com.utdallas.onlineshopping.action.product;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

/**
 * Created by prathyusha on 2/24/17.
 */
public class DeleteProductAction implements Action<Void> {
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String productId;

    @Inject
    public DeleteProductAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public DeleteProductAction withProductId( String productId )
    {
        this.productId = productId;
        return this;
    }

    @Override
    public Void invoke() {
        ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();

        try
        {
            productHibernateDAO.deleteByProductId( productId );
        }
        catch (HibernateException e)
        {
            throw new InternalErrorException(e.getCause().getMessage());
        }
        return null;
    }
}
