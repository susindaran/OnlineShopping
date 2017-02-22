package com.utdallas.onlineshopping.action.product;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CategoriesHibernateDAO;
import com.utdallas.onlineshopping.models.Category;
import com.utdallas.onlineshopping.payload.response.product.CategoriesResponse;
import com.utdallas.onlineshopping.payload.response.product.CategoryResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.List;

/**
 * Created by prathyusha on 2/21/17.
 */
public class GetCategoriesAction implements Action<CategoriesResponse> {
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String id;

    @Inject
    public GetCategoriesAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }


    @Override
    public CategoriesResponse invoke()
    {
        CategoriesHibernateDAO categoriesHibernateDAO = hibernateUtil.getCategoriesHibernateDAO();
        List<Category> categoryList= categoriesHibernateDAO.getAll();
        CategoriesResponse categoriesResponse = new CategoriesResponse();
        categoriesResponse.setCategories(categoryList);
        return categoriesResponse;
    }
}
