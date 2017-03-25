package com.betadevels.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Category;
import org.hibernate.SessionFactory;

import java.util.List;

public class CategoryHibernateDAO extends BaseHibernateDAO<Category> implements GenericDAO<Category>
{
    @Inject
    public CategoryHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public Optional<Category> findById(String id)
    {
        if( id != null )
        {
            return Optional.fromNullable(get(id));
        }
        else
        {
            return Optional.absent();
        }
    }

    public List<Category> getAll()
    {
        return currentSession().createCriteria(Category.class).list();
    }

    public void deleteByCategoryId(String categoryId)
    {
        Optional<Category> categoryOptional = Optional.fromNullable(get(categoryId));
        if( !categoryOptional.isPresent() )
        {
            throw new NotFoundException("Unable to find the given Category ID");
        }
        else
        {
            delete( categoryOptional.get() );
        }
    }
}
