package com.betadevels.onlineshopping.action.cart;


import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.payload.response.cart.CartItemsResponse;
import com.betadevels.onlineshopping.payload.response.cart.CartResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class GetParticularCartItemsAction implements Action<CartItemsResponse>
{
	private final HibernateUtil hibernateUtil;
	private ModelMapper modelMapper;
	private List<Long> cartIds;

	@Inject
	public GetParticularCartItemsAction( Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper )
	{
		this.hibernateUtil = hibernateUtilProvider.get();
		this.modelMapper = modelMapper;
	}

	public GetParticularCartItemsAction forCartIds( List<Long> cartIds )
	{
		this.cartIds = cartIds;
		return this;
	}

	@Override
	public CartItemsResponse invoke()
	{
		CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();
		List<Cart> cartItems = cartHibernateDAO.getCartItems( cartIds );

		List<CartResponse> cartResponses = cartItems.stream().map( cartItem -> modelMapper.map(cartItem, CartResponse.class ) ).collect(
				Collectors.toList() );

		double totalPrice = cartItems.stream()
		                             .mapToDouble( cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity() )
		                             .sum();

		double discounts = cartItems.stream().mapToDouble( cartItem ->
		{
			if ( cartItem.getOffer() != null )
			{
				return (cartItem.getOffer().getDiscount() * cartItem.getProduct().getPrice() / 100) * cartItem.getQuantity();
			}
			else
			{
				return 0;
			}
		} ).sum();

		CartItemsResponse.PriceDetails priceDetails = new CartItemsResponse.PriceDetails();
		priceDetails.setTotalPrice( totalPrice );
		priceDetails.setDiscounts( discounts );
		priceDetails.setAmountPayable( totalPrice - discounts );

		CartItemsResponse cartItemsResponse = new CartItemsResponse();
		cartItemsResponse.setCount( cartResponses.size() );
		cartItemsResponse.setCartItems( cartResponses );
		cartItemsResponse.setPriceDetails( priceDetails );
		return cartItemsResponse;
	}
}
