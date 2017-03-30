package com.betadevels.onlineshopping.action.cart;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.exceptions.BadRequestException;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.request.cart.UpdateCartRequest;
import com.betadevels.onlineshopping.payload.request.product.ProductRequest;
import com.betadevels.onlineshopping.payload.response.cart.AddProductToCartResponse;
import com.betadevels.onlineshopping.payload.response.cart.CartItemsResponse;
import com.betadevels.onlineshopping.payload.response.cart.CartResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
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
       // CustomerHibernateDAO customerHibernateDAO = hibernateUtil.getCustomerHibernateDAO();


        if( cartList.size() < 1 )
        {
            throw new NotFoundException( Collections.singletonList( "No cart item matching the given cart_id" ) );
        }

        Cart cart = cartList.get(0);



       /* String productId = cart.getProduct().getProductId();
        Long customerId = cart.getCustomer().getCustomerId();
        Optional<Product> productOptional = productHibernateDAO.findById(productId);
        Optional<Customer>customerOptional = customerHibernateDAO.findById(customerId);

        if( customerOptional.isPresent() && productOptional.isPresent() )
        {
            Customer customer = customerOptional.get();
            Product product = productOptional.get();

            if( updateCartRequest.getQuantity()>product.getQuantity())
                throw new BadRequestException("Requested quantity exceeds availability");
            if( updateCartRequest.getQuantity()!=null )
            {
                cart.setQuantity(updateCartRequest.getQuantity());
                product.setQuantity(product.getQuantity() - cart.getQuantity());
                productHibernateDAO.update(product);
            }
            Cart newCart = Cart.builder().customer( customer )
                    .product( product )
                    .quantity( updateCartRequest.getQuantity() )
                    .build();

            Cart updateCart = cartHibernateDAO.create(cart);


            CartItemsResponse updateItemsInCartResponse = new CartItemsResponse();
            updateItemsInCartResponse.setItemsInCart( cartHibernateDAO.countForCustomer( customer ) );
            updateItemsInCartResponse.setProductAdded( modelMapper.map( updateCart, CartResponse.class ) );
            return updateItemsInCartResponse ;


*/


        String tempcart = cart.getProduct().getProductId();
        List<Product> productList = productHibernateDAO.findByParams( Collections.singletonMap( "productId", tempcart ) );
        Product product = productList.get( 0 );

           if( updateCartRequest.getQuantity()>product.getQuantity())
               throw new BadRequestException("Requested quantity exceeds availability");
            if( updateCartRequest.getQuantity()!=null ) {
                cart.setQuantity(updateCartRequest.getQuantity());
                product.setQuantity(product.getQuantity() - cart.getQuantity());
                productHibernateDAO.update(product);
            }

            Cart newCart = cartHibernateDAO.update(cart);
            return modelMapper.map(newCart, CartItemsResponse.class);

    }
}
