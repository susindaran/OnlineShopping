package com.betadevels.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.models.Order;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class OrderHibernateDAO extends BaseHibernateDAO<Order> implements GenericDAO<Order>
{
    @Inject
    public OrderHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public List<Order> getOrdersOfCustomer( Customer customer, int page, int size )
    {
        Criteria criteria = criteria().add(Restrictions.eq("customer", customer));
        criteria.addOrder(org.hibernate.criterion.Order.desc("updatedAt"));
        criteria.setFirstResult( (page - 1) * size );
        criteria.setMaxResults( size );
        return criteria.list();
    }

    public Long countOfCustomer(Customer customer)
    {
    	if( customer != null )
	    {
		    return (Long) criteria().add( Restrictions.eq( "customer", customer) ).setProjection( Projections.rowCount() ).uniqueResult();
	    }
	    return count();
    }
}
