package com.utdallas.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Order;
import org.hibernate.SessionFactory;

public class OrderHibernateDAO extends BaseHibernateDAO<Order> implements GenericDAO<Order>
{
    @Inject
    public OrderHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }
}
