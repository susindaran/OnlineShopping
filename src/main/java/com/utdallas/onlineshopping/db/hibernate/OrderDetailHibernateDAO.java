package com.utdallas.onlineshopping.db.hibernate;

import com.google.inject.Inject;
import com.utdallas.onlineshopping.db.GenericDAO;
import com.utdallas.onlineshopping.models.OrderDetail;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class OrderDetailHibernateDAO extends BaseHibernateDAO<OrderDetail> implements GenericDAO<OrderDetail>
{
    @Inject
    public OrderDetailHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }


    public List<OrderDetail> getAll(int page, int size, String status)
    {
        Criteria criteria = currentSession().createCriteria(OrderDetail.class);

        if(status!=null && !"".equals(status))
            criteria.add(Restrictions.eq("orderDetailStatus",status));

        criteria.setFirstResult( (page - 1) * size );
        criteria.setMaxResults( size );
        return criteria.list();
    }

    public List<OrderDetail> getOrderDetailsByIds(List<Long> orderDetailId)
    {
        Criteria criteria = currentSession().createCriteria(OrderDetail.class);
        criteria.add(Restrictions.in("orderDetailId", orderDetailId));
        return criteria.list();
    }


}
