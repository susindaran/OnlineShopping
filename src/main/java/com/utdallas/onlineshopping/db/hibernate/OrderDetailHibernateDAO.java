package com.utdallas.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.OrderDetail;
import org.hibernate.SessionFactory;

public class OrderDetailHibernateDAO extends BaseHibernateDAO<OrderDetail> implements GenericDAO<OrderDetail>
{
    @Inject
    public OrderDetailHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }
}
