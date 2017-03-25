package com.betadevels.onlineshopping.action.cart;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.payload.response.cart.CartItemsResponse;
import com.betadevels.onlineshopping.payload.response.cart.CartResponse;
import com.betadevels.onlineshopping.payload.response.customer.CustomerResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GetItemsInCartAction implements Action<CartItemsResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private Long customerId;

    @Inject
    public GetItemsInCartAction(Provider<HibernateUtil> hibernateUtilProvider,
                                ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public GetItemsInCartAction forCustomerId( Long customerId )
    {
        this.customerId = customerId;
        return this;
    }

    @Override
    public CartItemsResponse invoke()
    {
        CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();

        Optional<Customer> customerOptional = customerHibernateDAO.findById(customerId);
        if( customerOptional.isPresent() )
        {
            Customer customer = customerOptional.get();
            List<Cart> cartItems = cartHibernateDAO.getCartItemsOfCustomer(customer);
            List<CartResponse> cartResponses = cartItems.stream().map(cartItem -> modelMapper.map(cartItem, CartResponse.class)).collect(Collectors.toList());

            CartItemsResponse cartItemsResponse = new CartItemsResponse();
            cartItemsResponse.setCount( cartResponses.size() );
            cartItemsResponse.setCartItems( cartResponses );
            cartItemsResponse.setCustomer( modelMapper.map( customer, CustomerResponse.class) );
            return cartItemsResponse;
        }
        throw new NotFoundException(Collections.singletonList("No customer matching the given customer_id"));
    }
}
