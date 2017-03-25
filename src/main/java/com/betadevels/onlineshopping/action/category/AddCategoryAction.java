package com.betadevels.onlineshopping.action.category;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CategoryHibernateDAO;

import com.betadevels.onlineshopping.exceptions.ConflictingRequestException;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.models.Category;

import com.betadevels.onlineshopping.payload.request.category.CategoryRequest;

import com.betadevels.onlineshopping.payload.response.category.CategoryResponse;

import com.betadevels.onlineshopping.util.HibernateUtil;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

@Slf4j
public class AddCategoryAction implements Action<CategoryResponse>
{

    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private CategoryRequest categoryRequest;

    @Inject
    public AddCategoryAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public AddCategoryAction withRequest(CategoryRequest categoryRequest)
    {
        this.categoryRequest = categoryRequest;
        return this;
    }

    @Override
    public CategoryResponse invoke()
    {
        CategoryHibernateDAO categoryHibernateDAO = hibernateUtil.getCategoryHibernateDAO();
        try
        {
            Category category = Category.builder()
                    .categoryId(categoryRequest.getCategoryId())
                    .categoryName(categoryRequest.getCategoryName())
                    .build();
            Category newCategory = categoryHibernateDAO.create(category);
            return modelMapper.map(newCategory, CategoryResponse.class);
        }
        catch (HibernateException e)
        {
            log.error(String.valueOf(e.getCause()));
            String errorMessage = e.getCause().getMessage();
            if( errorMessage.contains("Duplicate entry") )
                throw new ConflictingRequestException("Category ID already exists");
            throw new InternalErrorException(e.getMessage());
        }
    }
}
