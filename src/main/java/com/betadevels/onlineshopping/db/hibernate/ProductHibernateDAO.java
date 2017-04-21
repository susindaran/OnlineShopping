package com.betadevels.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Product;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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

    public List<Product> getAll(String categoryId, int page, int size)
    {
        Criteria criteria = currentSession().createCriteria(Product.class);
        if(!Strings.isNullOrEmpty(categoryId))
        {
            criteria.add(Restrictions.like("productId",categoryId+"%"));
        }
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

    public List<Product> searchProductsByName( String query )
    {
        Criteria criteria = criteria();
        criteria.add( Restrictions.like( "productName", '%' + query + '%' ) );
        criteria.addOrder( Order.asc( "productName" ) );

        return criteria.list();
    }
}
