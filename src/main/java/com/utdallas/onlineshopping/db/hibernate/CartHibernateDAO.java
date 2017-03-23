package com.utdallas.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Cart;
import com.utdallas.onlineshopping.models.Customer;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class CartHibernateDAO extends BaseHibernateDAO<Cart> implements GenericDAO<Cart>
{
    @Inject
    public CartHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public List<Cart> getCartItemsOfCustomer(Customer customer)
    {
        return criteria().add(Restrictions.eq("customer", customer)).list();
    }
}
