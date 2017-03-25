package com.betadevels.onlineshopping.action.cart;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.payload.response.cart.AddProductToCartResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;

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
