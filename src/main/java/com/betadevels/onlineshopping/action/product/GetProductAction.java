package com.betadevels.onlineshopping.action.product;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.response.product.ProductResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

public class GetProductAction implements Action<ProductResponse>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String productId;

    @Inject
    public GetProductAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public GetProductAction forProductId(String productId)
    {
        this.productId = productId;
        return this;
    }

    @Override
    public ProductResponse invoke()
    {
        ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();
        List<Product> productList = productHibernateDAO.findByParams(Collections.singletonMap("productId", productId));
        if( productList.size() == 1 )
            return modelMapper.map( productList.get(0), ProductResponse.class );
        else
            throw new NotFoundException("No Product matching the given ID");
    }
}
