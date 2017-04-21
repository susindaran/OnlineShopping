package com.betadevels.onlineshopping.action.product;

import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.exceptions.BadRequestException;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.response.product.AllProductsResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

public class SearchProductsAction implements Action<AllProductsResponse>
{
	private final HibernateUtil hibernateUtil;
	private ModelMapper modelMapper;
	private String query;

	@Inject
	public SearchProductsAction( Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper )
	{
		this.hibernateUtil = hibernateUtilProvider.get();
		this.modelMapper = modelMapper;
	}

	public SearchProductsAction withQuery( String query )
	{
		this.query = query;
		return this;
	}

	@Override
	public AllProductsResponse invoke()
	{
		ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();

		if( Strings.isNullOrEmpty( query ) )
		{
			throw new BadRequestException( Collections.singletonList( "Search query ") );
		}

		List<Product> products = productHibernateDAO.searchProductsByName( query );
		return AllProductsResponse.builder().products( products ).build();
	}
}
