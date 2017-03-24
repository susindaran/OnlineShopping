package com.utdallas.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.enumerations.TransactionType;
import com.utdallas.onlineshopping.models.Order;
import com.utdallas.onlineshopping.models.Payment;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class PaymentHibernateDAO extends BaseHibernateDAO<Payment> implements GenericDAO<Payment>
{
    @Inject
    public PaymentHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public List<Payment> getPaymentsOfOrder(Order order )
    {
        return criteria().add( Restrictions.eq( "order", order ) ).list();
    }

    public Payment getPaymentOfOrderDetail( Order order, String productId, TransactionType type, String reason)
    {
        return ( Payment ) criteria().add( Restrictions.eq( "order", order ) )
                                     .add( Restrictions.eq( "ref1", productId ) )
                                     .add( Restrictions.eq( "transactionType", type.getType() ) )
                                     .add( Restrictions.eq( "reason", reason ) )
                                     .uniqueResult();
    }
}
