package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Product;
import org.hibernate.SessionFactory;

import java.util.Collections;
import java.util.List;

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

    @Override
    public Product update(Product product)
    {
        return persist(product);
    }

    @Override
    public void delete(Product product)
    {
        currentSession().delete( product );
    }

    public void deleteByProductId(String productId)
    {
        List<Product> productList = findByParams(Collections.singletonMap("productId", productId));
        if( productList.size() == 0 )
        {
            throw new NotFoundException("Unable to find product for the given ID");
        }
        else
        {
            delete( productList.get( 0 ) );
        }
    }

    @Override
    public Product merge(Product obj) {
        return null;
    }
}
