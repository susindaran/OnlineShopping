package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Shipment;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.Collections;
import java.util.List;

public class ShipmentHibernateDAO extends BaseHibernateDAO<Shipment> implements GenericDAO<Shipment>
{
    @Inject
    public ShipmentHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public List<Shipment> getAll(int page, int size, String status)
    {
        Criteria criteria = currentSession().createCriteria(Shipment.class);

        if(status!=null && !"".equals(status))
        criteria.add(Restrictions.eq("status",status));

        criteria.setFirstResult( (page - 1) * size );
        criteria.setMaxResults( size );
        return criteria.list();
    }
    public List<Shipment> getAllShipments(int page, int size)
    {
        Criteria criteria = currentSession().createCriteria(Shipment.class);
        criteria.setFirstResult( (page - 1) * size );
        criteria.setMaxResults( size );
        return criteria.list();
    }


}
