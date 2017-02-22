package com.utdallas.onlineshopping.action.product;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CategoriesHibernateDAO;
import com.utdallas.onlineshopping.exceptions.ConflictingRequestException;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.models.Category;
import com.utdallas.onlineshopping.payload.request.product.UpdateCategoryRequest;
import com.utdallas.onlineshopping.payload.response.product.CategoryResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

/**
 * Created by prathyusha on 2/21/17.
 */
@Slf4j
public class UpdateCategoriesAction implements Action<CategoryResponse> {
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private UpdateCategoryRequest updateCategoriesRequest;
    private String categoryId;

    public UpdateCategoriesAction withRequest(UpdateCategoryRequest updateCategoriesRequest)
    {
        this.updateCategoriesRequest = updateCategoriesRequest;
        return this;
    }

    public UpdateCategoriesAction withId(String id)
    {
        this.categoryId = id;
        return this;
    }

    @Inject
    public UpdateCategoriesAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }
@Override
public CategoryResponse invoke()
{
    CategoriesHibernateDAO categoriesHibernateDAO = hibernateUtil.getCategoriesHibernateDAO();
    Category category = categoriesHibernateDAO.findById(categoryId).get();

    try
    {
        if( !Strings.isNullOrEmpty(updateCategoriesRequest.getCategoryName()) )
            category.setCategoryName(updateCategoriesRequest.getCategoryName());
        Category newCategory = categoriesHibernateDAO.update(category);
        return modelMapper.map(newCategory, CategoryResponse.class);
    }
    catch( HibernateException e )
    {
        log.error(String.valueOf(e.getCause()));
        String errorMessage = e.getCause().getMessage();
        if( errorMessage.contains("Duplicate entry") )
            throw new ConflictingRequestException("Category ID already exists");
        throw new InternalErrorException(e.getMessage());
    }
}


}
