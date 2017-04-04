package com.betadevels.onlineshopping.db.hibernate;

import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.models.Subscription;
import com.google.inject.Inject;
import org.hibernate.SessionFactory;

public class SubscriptionHibernateDAO extends BaseHibernateDAO<Subscription> implements GenericDAO<Subscription>
{
	@Inject
	public SubscriptionHibernateDAO( SessionFactory sessionFactory )
	{
		super( sessionFactory );
	}
}
