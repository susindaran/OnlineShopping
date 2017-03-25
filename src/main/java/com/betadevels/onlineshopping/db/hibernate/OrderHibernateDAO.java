package com.betadevels.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.models.Order;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class OrderHibernateDAO extends BaseHibernateDAO<Order> implements GenericDAO<Order>
{
    @Inject
    public OrderHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public List<Order> getAllOrders(Customer customer)
    {
        Criteria criteria = criteria().add(Restrictions.eq("customer", customer));
        criteria.addOrder(org.hibernate.criterion.Order.desc("updatedAt"));
        return criteria.list();
    }
}
