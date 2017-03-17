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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private String createLink(int page, int size)
    {
        return requestURL + "?page=" + page + "&size=" + size;
    }

    private Map<String, String> preparePaginationLinks(Long totalCount)
    {
        Map<String, String> linksMap = new HashMap<>();
        int pages = Math.toIntExact((totalCount / this.size) + (totalCount % this.size == 0 ? 0 : 1));
        linksMap.put("first", createLink(1, this.size));
        linksMap.put("prev", this.page > 1 ? createLink(this.page - 1, this.size) : null);
        linksMap.put("next", this.page * this.size < totalCount ? createLink( this.page + 1, this.size ) : null);
        linksMap.put("last", createLink(pages, size));

        return linksMap;
    }

    @Override
    public AllProductsResponse invoke()
    {
        AllProductsValidator.validateQueryParams(page, size);
        ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();
        List<Product> productList = productHibernateDAO.getAll( page, size );
        Long totalCount = productHibernateDAO.count();
        int count = productList.size();

        AllProductsResponse allProductsResponse = AllProductsResponse.builder()
                .products(productList)
                .count(count)
                .totalCount(totalCount)
                .links( preparePaginationLinks( totalCount ))
                .build();

        return allProductsResponse;
    }
}
