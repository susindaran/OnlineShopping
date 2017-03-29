package com.betadevels.onlineshopping.action.cart;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.payload.request.cart.DeleteItemsInCartRequest;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;


/**
 * Created by girijagodbole on 3/28/17.
 */
public class DeleteItemsInCartAction implements Action<Void> {
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private int cartId;
    private Long customerId;

    @Inject
    public DeleteItemsInCartAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public DeleteItemsInCartAction withCartId(int cartId )
    {
        this.cartId = cartId;
        return this;
    }
    public DeleteItemsInCartAction forCustomerId(Long customerId)
    {
        this.customerId = customerId;
        return this;
    }

    @Override
    public Void invoke() {
        CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();

        try {

                cartHibernateDAO.deleteByCartId(cartId);




        }
        catch (HibernateException e)
        {
            throw new InternalErrorException(e.getCause().getMessage());
        }
        return null;
    }
}

