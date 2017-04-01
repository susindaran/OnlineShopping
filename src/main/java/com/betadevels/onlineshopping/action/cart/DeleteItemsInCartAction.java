package com.betadevels.onlineshopping.action.cart;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.hibernate.HibernateException;

import java.util.Collections;

public class DeleteItemsInCartAction implements Action<Void>
{
    private HibernateUtil hibernateUtil;
    private Long cartId;

    @Inject
    public DeleteItemsInCartAction(Provider<HibernateUtil> hibernateUtilProvider)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
    }

    public DeleteItemsInCartAction withCartId(Long cartId )
    {
        this.cartId = cartId;
        return this;
    }

    @Override
    public Void invoke()
    {
        CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();
        ProductHibernateDAO productHibernateDAO=this.hibernateUtil.getProductHibernateDAO();
        Optional<Cart> cartOptional= cartHibernateDAO.findById(cartId);

        if( !cartOptional.isPresent() )
        {
	        throw new NotFoundException( Collections.singletonList( "No cart item matching the given cart_id" ) );
        }

        Cart cart =  cartOptional.get( );
        Product product = cart.getProduct();
        try
        {
            cartHibernateDAO.delete(cart);

            //Adding the quantity to product which the customer deleted from cart
            product.setQuantity(product.getQuantity() + cart.getQuantity());
            productHibernateDAO.update(product);
        }
        catch(HibernateException e)
        {
	        throw new InternalErrorException( e.getCause().getCause() );
        }
        return null;
    }
}

