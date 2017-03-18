package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Product;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

import java.util.Collections;
import java.util.List;

public class ProductHibernateDAO extends BaseHibernateDAO<Product> implements GenericDAO<Product>
{
    @Inject
    public ProductHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public Optional<Product> findById(String id)
    {
        List<Product> productList = findByParams(Collections.singletonMap("productId", id));
        if( productList.size() > 0 )
        {
            return Optional.fromNullable( productList.get(0) );
        }
        else
        {
            return Optional.absent();
        }
    }

    public List<Product> getAll(int page, int size)
    {
        Criteria criteria = currentSession().createCriteria(Product.class);
        criteria.setFirstResult( (page - 1) * size );
        criteria.setMaxResults( size );
        return criteria.list();
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
}
