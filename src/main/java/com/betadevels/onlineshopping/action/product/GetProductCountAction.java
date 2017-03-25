package com.betadevels.onlineshopping.action.product;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.payload.response.product.AllProductsResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;

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
