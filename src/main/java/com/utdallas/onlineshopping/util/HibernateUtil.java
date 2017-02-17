package com.utdallas.onlineshopping.util;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.db.hibernate.AddressHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.TaxDetailsHibernateDAO;
import lombok.Getter;

@Getter
public class HibernateUtil
{
    private final CustomerHibernateDAO customerHibernateDAO;
    private final TaxDetailsHibernateDAO taxDetailsHibernateDAO;
    private final AddressHibernateDAO addressHibernateDAO;

    @Inject
    public HibernateUtil(Provider<CustomerHibernateDAO> customerHibernateDAOProvider,
                         Provider<TaxDetailsHibernateDAO> taxDetailsHibernateDAOProvider,
                         Provider<AddressHibernateDAO> addressHibernateDAOProvider)
    {
        this.customerHibernateDAO = customerHibernateDAOProvider.get();
        this.taxDetailsHibernateDAO = taxDetailsHibernateDAOProvider.get();
        this.addressHibernateDAO = addressHibernateDAOProvider.get();
    }
}
