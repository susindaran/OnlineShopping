package com.utdallas.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Shipment;
import org.hibernate.SessionFactory;

public class ShipmentHibernateDAO extends BaseHibernateDAO<Shipment> implements GenericDAO<Shipment>
{
    @Inject
    ShipmentHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }
}
