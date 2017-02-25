package com.utdallas.onlineshopping.action.category;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CategoryHibernateDAO;
import com.utdallas.onlineshopping.models.Category;
import com.utdallas.onlineshopping.payload.response.category.CategoriesResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
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
