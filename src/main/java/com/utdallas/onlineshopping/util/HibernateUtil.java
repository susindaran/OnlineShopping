package com.utdallas.onlineshopping.util;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import lombok.Getter;

@Getter
public class HibernateUtil
{
    private final CustomerHibernateDAO customerHibernateDAO;

    @Inject
    public HibernateUtil(Provider<CustomerHibernateDAO> customerHibernateDAOProvider)
    {
        this.customerHibernateDAO = customerHibernateDAOProvider.get();
    }
}
