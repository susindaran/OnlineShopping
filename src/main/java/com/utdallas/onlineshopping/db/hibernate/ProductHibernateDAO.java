package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Product;
import org.hibernate.SessionFactory;

public class ProductHibernateDAO extends BaseHibernateDAO<Product> implements GenericDAO<Product>
{
    @Inject
    public ProductHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Override
    public Product create(Product product)
    {
        return persist(product);
    }

    @Override
    public Optional<Product> findById(Long id)
    {
        if( id != null )
        {
            return Optional.fromNullable( get(id) );
        }
        else
        {
            return Optional.absent();
        }
    }

    public Optional<Product> findById(String id)
    {
        if( id != null )
        {
            return Optional.fromNullable( get(id) );
        }
        else
        {
            return Optional.absent();
        }
    }

    @Override
    public Product update(Product product)
    {
        Product newProduct = persist(product);
        this.currentSession().flush();
        return newProduct;
    }

    @Override
    public void delete(Product obj) {

    }

    @Override
    public Product merge(Product obj) {
        return null;
    }
}
