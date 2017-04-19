package com.betadevels.onlineshopping.db.hibernate;

import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.models.Subscription;
import com.google.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Collections;
import java.util.List;

public class SubscriptionHibernateDAO extends BaseHibernateDAO<Subscription> implements GenericDAO<Subscription>
{

	public SubscriptionHibernateDAO(SessionFactory sessionFactory)
	{

		super(sessionFactory);
	}

	public List<Subscription> getSubscriptionsOfUser(int page, int size, Customer customer)
	{
		Criteria criteria = currentSession().createCriteria( Subscription.class );
		criteria.add( Restrictions.eq( "customer", customer ) );

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
	public List<Subscription> getSubscriptionsByIds(List<Long> subscriptionId)
	{
		Criteria criteria = currentSession().createCriteria(Subscription.class);
		criteria.add(Restrictions.in("subscriptionId",subscriptionId));
		return criteria.list();
	}


}