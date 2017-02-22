package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Category;
import org.hibernate.SessionFactory;

import java.util.List;

public class CategoryHibernateDAO extends BaseHibernateDAO<Category> implements GenericDAO<Category>
{
    @Inject
    public CategoryHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Override
    public Category create(Category category)
    {
        return persist(category);
    }

    @Override
    public Optional<Category> findById(Long id)
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

    @Override
    public Category update(Category category)
    {
        Category category1 = persist(category);
        this.currentSession().flush();
        return category1;
    }

    @Override
    public void delete(Category category) {

    }

    @Override
    public Category merge(Category category) {
        return null;
    }
}
