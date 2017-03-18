package com.utdallas.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Cart;
import org.hibernate.SessionFactory;

public class CartHibernateDAO extends BaseHibernateDAO<Cart> implements GenericDAO<Cart>
{
    @Inject
    public CartHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }
}
