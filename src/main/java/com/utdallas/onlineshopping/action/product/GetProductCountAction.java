package com.utdallas.onlineshopping.action.product;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.utdallas.onlineshopping.payload.response.product.AllProductsResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;

public class GetProductCountAction implements Action<AllProductsResponse>
{
    private final HibernateUtil hibernateUtil;

    @Inject
    public GetProductCountAction(Provider<HibernateUtil> hibernateUtilProvider)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
    }

    @Override
    public AllProductsResponse invoke()
    {
        ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();
        Long totalCount = productHibernateDAO.count();
        return AllProductsResponse.builder().totalCount( totalCount ).build();
    }
}
