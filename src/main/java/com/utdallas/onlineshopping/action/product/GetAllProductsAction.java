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

import static com.utdallas.onlineshopping.util.Utility.preparePaginationLinks;

public class GetAllProductsAction implements Action<AllProductsResponse>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String requestURL;
    private int page, size;

    @Inject
    public GetAllProductsAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public GetAllProductsAction withRequestURL(String requestURL)
    {
        this.requestURL = requestURL;
        return this;
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
        AllProductsValidator.validatePaginateParameters(page, size);
        ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();
        List<Product> productList = productHibernateDAO.getAll( page, size );
        Long totalCount = productHibernateDAO.count();
        int count = productList.size();

        return AllProductsResponse.builder()
                                  .products(productList)
                                  .count(count)
                                  .totalCount(totalCount)
                                  .links( preparePaginationLinks( totalCount, size, page, requestURL ))
                                  .build();
    }
}
