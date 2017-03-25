package com.utdallas.onlineshopping.action.cart;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CartHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.payload.response.cart.AddProductToCartResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;

import java.util.Collections;

public class GetItemsInCartCountAction implements Action<AddProductToCartResponse>
{
	private final HibernateUtil hibernateUtil;
	private Long customerId;

	@Inject
	public GetItemsInCartCountAction( Provider<HibernateUtil> hibernateUtilProvider )
	{
		this.hibernateUtil = hibernateUtilProvider.get();
	}

	public GetItemsInCartCountAction forCustomerId(Long customerId)
	{
		this.customerId = customerId;
		return this;
	}

	@Override
	public AddProductToCartResponse invoke()
	{
		CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();
		Optional<Customer> customerOptional = customerHibernateDAO.findById( customerId );
		if( customerOptional.isPresent() )
		{
			CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();
			AddProductToCartResponse addProductToCartResponse = new AddProductToCartResponse();
			addProductToCartResponse.setItemsInCart( cartHibernateDAO.countForCustomer( customerOptional.get() ) );
			return addProductToCartResponse;
		}
		throw new NotFoundException( Collections.singletonList( "No customer matching the given customer_id" ) );
	}
}
