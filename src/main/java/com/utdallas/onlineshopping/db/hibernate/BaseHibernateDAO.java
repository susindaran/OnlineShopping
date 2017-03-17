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
    BaseHibernateDAO(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public T create(T obj)
    {
        return persist( obj );
    }

    public Optional<T> findById(Long id)
    {
        if( id != null )
        {
            return Optional.fromNullable( get( id ) );
        }
        else
        {
            return Optional.absent();
        }
    }

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

    public T update(T obj)
    {
        T persisted = persist(obj);
        //Flushing the session explicitly here because hibernate sometimes decides not to
        //update the entity immediately, which causes any exception not to be thrown until
        //the transaction is committed.
        currentSession().flush();
        return persisted;
    }

    public void delete(T obj)
    {
        currentSession().delete( obj );
    }

    public Long count()
    {
        return (Long) criteria().setProjection(Projections.rowCount()).uniqueResult();
    }
}
