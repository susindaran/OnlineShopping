package com.utdallas.onlineshopping.action.cart;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CartHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Cart;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.payload.response.cart.CartItemsResponse;
import com.utdallas.onlineshopping.payload.response.cart.CartResponse;
import com.utdallas.onlineshopping.payload.response.customer.CustomerResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
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
