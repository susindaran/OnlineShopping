package com.betadevels.onlineshopping.db.hibernate;

import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Product;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.models.Customer;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Collections;
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

    public Long countForCustomer( Customer customer )
    {
        return (Long) criteria().add( Restrictions.eq( "customer", customer) ).setProjection( Projections.rowCount() ).uniqueResult();
    }
    public void deleteByCartId(Long cartId)
    {
        Optional<Cart> cartList = findById(cartId);
        if( cartList.get().equals("null") )
        {
            throw new NotFoundException("Unable to find item in cart for the given ID");
        }
        else
        {
            delete( cartList.get());
        }
    }
}
