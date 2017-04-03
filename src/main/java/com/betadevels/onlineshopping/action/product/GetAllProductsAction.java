package com.betadevels.onlineshopping.action.product;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.response.product.AllProductsResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.betadevels.onlineshopping.validators.product.AllProductsValidator;
import org.modelmapper.ModelMapper;

import java.util.List;

import static com.betadevels.onlineshopping.util.Utility.preparePaginationLinks;

public class GetAllProductsAction implements Action<AllProductsResponse>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String requestURL;
    private int page, size;
    private String categoryId;

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

    public GetAllProductsAction forCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
        return this;
    }

    @Override
    public AllProductsResponse invoke()
    {
        AllProductsValidator.validatePaginateParameters(page, size);
        ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();
        List<Product> productList = productHibernateDAO.getAll( categoryId, page, size );
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
