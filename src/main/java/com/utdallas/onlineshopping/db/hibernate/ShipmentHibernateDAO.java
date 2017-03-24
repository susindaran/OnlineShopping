package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.Shipment;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

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
        criteria.addOrder( Order.asc( "updatedAt" ) );

        if( !Strings.isNullOrEmpty( status ) )
        {
            criteria.add(Restrictions.eq("status",status));
        }

        criteria.setFirstResult( (page - 1) * size );
        criteria.setMaxResults( size );
        return criteria.list();
    }

    public List<Shipment> getShipmentsByIds(List<Long> shipmentIds)
    {
        Criteria criteria = currentSession().createCriteria(Shipment.class);
        criteria.add(Restrictions.in("shipmentId",shipmentIds));
        return criteria.list();
    }

    public void reloadShipment(Shipment shipment)
    {
        currentSession().flush();
        currentSession().refresh( shipment );
    }

    public Long countWithStatus(String status)
    {
    	if( !Strings.isNullOrEmpty( status ))
	    {
		    return (Long) criteria().add( Restrictions.eq( "status", status) ).setProjection( Projections.rowCount() ).uniqueResult();
	    }
	    return count();
    }
}
