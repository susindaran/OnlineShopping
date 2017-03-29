package com.betadevels.onlineshopping.action.cart;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.request.cart.UpdateCartRequest;
import com.betadevels.onlineshopping.payload.request.product.ProductRequest;
import com.betadevels.onlineshopping.payload.response.cart.CartItemsResponse;
import com.betadevels.onlineshopping.payload.response.cart.CartResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vidya on 3/28/17.
 */


@Slf4j
public class UpdateItemsInCartAction implements Action<CartItemsResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private UpdateCartRequest updateCartRequest;
    private int cartId;
    private ProductRequest productRequest;

    public UpdateItemsInCartAction withRequest(UpdateCartRequest updateCartRequest)
    {
        this.updateCartRequest = updateCartRequest;
        this.productRequest = productRequest;
        return this;
    }

    public  UpdateItemsInCartAction withId(int cartId)
    {
        this.cartId = cartId;
        return this;
    }

    @Inject
    public  UpdateItemsInCartAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public CartItemsResponse invoke()
    {
        CartHibernateDAO cartHibernateDAO = hibernateUtil.getCartHibernateDAO();
        List<Cart> cartList = cartHibernateDAO.findByParams( Collections.singletonMap( "cartId", this.cartId ) );
        ProductHibernateDAO productHibernateDAO = hibernateUtil.getProductHibernateDAO();


        if( cartList.size() < 1 )
        {
            throw new NotFoundException( Collections.singletonList( "No cart item matching the given cart_id" ) );
        }

        Cart cart = cartList.get(0);
        String tempcart = cart.getProduct().getProductId();
        List<Product> productList = productHibernateDAO.findByParams( Collections.singletonMap( "productId", tempcart ) );
        Product product = productList.get( 0 );
        try
        {

           if( updateCartRequest.getQuantity()>product.getQuantity())
               Collections.singletonList("Product out of stock");
           else
            if( updateCartRequest.getQuantity()!=null ) {
                cart.setQuantity(updateCartRequest.getQuantity());
                product.setQuantity(product.getQuantity() - cart.getQuantity());
            }

            Cart newCart = cartHibernateDAO.update(cart);
            return modelMapper.map(newCart, CartItemsResponse.class);
        }
        catch( HibernateException e )
        {
            log.error(String.valueOf(e.getCause()));
            throw new InternalErrorException(e.getMessage());
        }
    }
}
