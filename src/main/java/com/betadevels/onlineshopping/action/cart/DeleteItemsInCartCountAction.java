package com.betadevels.onlineshopping.action.cart;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.response.cart.AddProductToCartResponse;
import com.betadevels.onlineshopping.payload.response.cart.DeleteProductFromCartResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Collections;
import java.util.List;

/**
 * Created by girijagodbole on 3/30/17.
 */
public class DeleteItemsInCartCountAction implements Action<DeleteProductFromCartResponse>
{
    private final HibernateUtil hibernateUtil;
    private Long customerId;
    private int cartId;

    @Inject
    public DeleteItemsInCartCountAction( Provider<HibernateUtil> hibernateUtilProvider )
    {
        this.hibernateUtil = hibernateUtilProvider.get();
    }

    public DeleteItemsInCartCountAction forCartId(int cart_id)
    {
        this.cartId = cart_id;
        return this;
    }

    @Override
    public DeleteProductFromCartResponse invoke()
    {
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();
        CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();
        List<Cart> cartList= cartHibernateDAO.findByParams(Collections.singletonMap("cartId", this.cartId));


        Cart cart1 =  cartList.get( 0 );

        Long cust_id= cart1.getCustomer().getCustomerId();

        Optional<Customer> customerOptional = customerHibernateDAO.findById(cust_id);

        if( customerOptional.isPresent() )
        {

            DeleteProductFromCartResponse deleteProductFromCartResponse = new DeleteProductFromCartResponse();
            deleteProductFromCartResponse.setItemsInCart( cartHibernateDAO.countForCustomer( customerOptional.get() ) );
            return deleteProductFromCartResponse;
        }
        throw new NotFoundException( Collections.singletonList( "No customer matching the given customer_id" ) );
    }
}
