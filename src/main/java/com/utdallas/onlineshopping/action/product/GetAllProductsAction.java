package com.utdallas.onlineshopping.action.product;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.utdallas.onlineshopping.models.Product;
import com.utdallas.onlineshopping.payload.response.product.AllProductsResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import com.utdallas.onlineshopping.validators.product.AllProductsValidator;
import org.modelmapper.ModelMapper;

import java.util.List;

public class GetAllProductsAction implements Action<AllProductsResponse>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private int page, size;

    @Inject
    public GetAllProductsAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public GetAllProductsAction withPaginateDetails(int page, int size)
    {
        this.page = page;
        this.size = size;
        return this;
    }

    @Override
    public AllProductsResponse invoke()
    {
        AllProductsValidator.validateQueryParams(page, size);
        ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();
        List<Product> productList = productHibernateDAO.getAll( page, size );
        AllProductsResponse allProductsResponse = new AllProductsResponse();
        allProductsResponse.setProducts( productList );
        return allProductsResponse;
    }
}
