package com.betadevels.onlineshopping.action.category;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CategoryHibernateDAO;
import com.betadevels.onlineshopping.exceptions.InternalErrorException;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

public class DeleteCategoryAction implements Action<Void>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String categoryId;

    @Inject
    public DeleteCategoryAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public DeleteCategoryAction withCategoryId(String categoryId )
    {
        this.categoryId = categoryId;
        return this;
    }

    @Override
    public Void invoke()
    {
        CategoryHibernateDAO categoryHibernateDAO = this.hibernateUtil.getCategoryHibernateDAO();

        try
        {
            categoryHibernateDAO.deleteByCategoryId( categoryId );
        }
        catch (HibernateException e)
        {
            throw new InternalErrorException(e.getCause().getMessage());
        }

        return null;
    }
}

