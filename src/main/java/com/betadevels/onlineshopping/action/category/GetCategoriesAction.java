package com.betadevels.onlineshopping.action.category;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CategoryHibernateDAO;
import com.betadevels.onlineshopping.models.Category;
import com.betadevels.onlineshopping.payload.response.category.CategoriesResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.List;

public class GetCategoriesAction implements Action<CategoriesResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;

    @Inject
    public GetCategoriesAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoriesResponse invoke()
    {
        CategoryHibernateDAO categoryHibernateDAO = hibernateUtil.getCategoryHibernateDAO();
        List<Category> categoryList= categoryHibernateDAO.getAll();
        CategoriesResponse categoriesResponse = new CategoriesResponse();
        categoriesResponse.setCategories(categoryList);
        return categoriesResponse;
    }
}
