package com.utdallas.onlineshopping.db.hibernate;

import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BaseHibernateDAO<T> extends AbstractDAO<T>
{
    private SessionFactory sessionFactory;
    BaseHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public abstract T create(T obj);

    public abstract Optional<T> findById(Long id);

    public List<T> findByParams(Map<String, Object>params)
    {
        Criteria criteria = criteria();
        log.debug("Executing findByParams");
        if(params != null)
        {
            for (Map.Entry<String, Object> entry : params.entrySet())
            {
                criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
            }
        }
        return criteria.list();
    }

    public abstract T update(T obj);

    public abstract void delete(T obj);

    public long count()
    {
        return (Long) criteria().setProjection(Projections.rowCount()).uniqueResult();
    }

    public abstract T merge(T obj);
}
