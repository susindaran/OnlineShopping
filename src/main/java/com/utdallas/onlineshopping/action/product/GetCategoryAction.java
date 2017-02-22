package com.utdallas.onlineshopping.action.product;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Category;
import com.utdallas.onlineshopping.payload.response.product.CategoryResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

/**
 * Created by prathyusha on 2/21/17.
 */
public class GetCategoryAction implements Action<CategoryResponse> {
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String id;

    @Inject
    public GetCategoryAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public GetCategoryAction withId(String id )
    {
        this.id = id;
        return this;
    }

    public CategoryResponse invoke()
    {
        Optional<Category> categoriesOptional = hibernateUtil.getCategoriesHibernateDAO().findById(id);
        if( categoriesOptional.isPresent() )
        {
            return modelMapper.map(categoriesOptional.get(), CategoryResponse.class);
        }
        else
        {
            throw new NotFoundException("No category matching the given ID");
        }
    }
}
