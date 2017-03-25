package com.betadevels.onlineshopping.action.product;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.request.product.ProductRequest;
import com.betadevels.onlineshopping.payload.response.product.ProductResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

@Slf4j
public class UpdateProductAction implements Action<ProductResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private ProductRequest productRequest;
    private String productId;

    public UpdateProductAction withRequest(ProductRequest productRequest)
    {
        this.productRequest = productRequest;
        return this;
    }

    public UpdateProductAction withId(String id)
    {
        this.productId = id;
        return this;
    }

    @Inject
    public UpdateProductAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductResponse invoke()
    {
        ProductHibernateDAO productHibernateDAO = hibernateUtil.getProductHibernateDAO();
	    List<Product> productList = productHibernateDAO.findByParams( Collections.singletonMap( "productId", this.productId ) );

	    if( productList.size() < 1 )
	    {
	    	throw new NotFoundException( Collections.singletonList( "No product matching the given product_id" ) );
	    }

	    Product product = productList.get( 0 );
	    try
        {
        	if( !Strings.isNullOrEmpty( productRequest.getCategoryPrefix() ) )
        		product.setProductId( String.format("%s%010d", productRequest.getCategoryPrefix(), product.getId()) );
            if( !Strings.isNullOrEmpty(productRequest.getProductName()) )
                product.setProductName(productRequest.getProductName());
            if( !Strings.isNullOrEmpty(productRequest.getProductDescription()) )
                product.setDescription(productRequest.getProductDescription());
            if( productRequest.getQuantity()!=null )
                product.setQuantity(productRequest.getQuantity());
            if( productRequest.getPrice()!=null )
                product.setPrice(productRequest.getPrice());

            Product newProduct = productHibernateDAO.update(product);
            return modelMapper.map(newProduct, ProductResponse.class);
        }
        catch( HibernateException e )
        {
            log.error(String.valueOf(e.getCause()));
            throw new InternalErrorException(e.getMessage());
        }
    }
}
