package com.utdallas.onlineshopping.action.category;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.action.card.DeleteCardDetailAction;
import com.utdallas.onlineshopping.db.hibernate.CardDetailHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.CategoryHibernateDAO;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

/**
 * Created by vidya on 2/24/17.
 */
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

