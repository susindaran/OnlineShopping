package com.utdallas.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Payment;
import org.hibernate.SessionFactory;

public class PaymentHibernateDAO extends BaseHibernateDAO<Payment> implements GenericDAO<Payment>
{
    @Inject
    public PaymentHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }
}
