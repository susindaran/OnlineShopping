package com.betadevels.onlineshopping.action.cart;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.exceptions.BadRequestException;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.request.cart.UpdateCartRequest;
import com.betadevels.onlineshopping.payload.response.cart.CartResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import java.util.Collections;

@Slf4j
public class UpdateItemsInCartAction implements Action<CartResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private UpdateCartRequest updateCartRequest;
    private Long cartId;

    @Inject
    public  UpdateItemsInCartAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public UpdateItemsInCartAction withRequest(UpdateCartRequest updateCartRequest)
    {
        this.updateCartRequest = updateCartRequest;
        return this;
    }

    public  UpdateItemsInCartAction forCartId( Long cartId )
    {
        this.cartId = cartId;
        return this;
    }

    @Override
    public CartResponse invoke()
    {
        CartHibernateDAO cartHibernateDAO = hibernateUtil.getCartHibernateDAO();
	    ProductHibernateDAO productHibernateDAO = hibernateUtil.getProductHibernateDAO();

        Optional<Cart> cartOptional = cartHibernateDAO.findById(this.cartId);
        if( !cartOptional.isPresent() )
        {
            throw new NotFoundException( Collections.singletonList( "No cart item matching the given cart_id" ) );
        }

        Cart cart = cartOptional.get();
	    int quantityDifference = updateCartRequest.getQuantity() - cart.getQuantity();

        if( quantityDifference != 0 )
        {
	        Product product = cart.getProduct();
	        if( quantityDifference > product.getQuantity() )
	        {
		        throw new BadRequestException( Collections.singletonList( "Requested quantity exceeds availability" ) );
	        }

            cart.setQuantity(updateCartRequest.getQuantity());
            product.setQuantity(product.getQuantity() + quantityDifference);
            try
            {
	            productHibernateDAO.update(product);
	            cartHibernateDAO.update(cart);
            }
            catch ( HibernateException e )
            {
            	throw new InternalErrorException( e.getCause().getCause() );
            }
        }
	    return modelMapper.map(cart, CartResponse.class);
    }
}
