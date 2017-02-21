package com.utdallas.onlineshopping.util;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.db.hibernate.*;
import lombok.Getter;

@Getter
public class HibernateUtil
{
    private final CustomerHibernateDAO customerHibernateDAO;
    private final TaxDetailsHibernateDAO taxDetailsHibernateDAO;
    private final AddressHibernateDAO addressHibernateDAO;
    private final CardDetailHibernateDAO cardDetailHibernateDAO;
    private final ProductHibernateDAO productHibernateDAO;

    @Inject
    public HibernateUtil(Provider<CustomerHibernateDAO> customerHibernateDAOProvider,
                         Provider<TaxDetailsHibernateDAO> taxDetailsHibernateDAOProvider,
                         Provider<AddressHibernateDAO> addressHibernateDAOProvider,
                         Provider<CardDetailHibernateDAO> cardDetailHibernateDAOProvider,
                         Provider<ProductHibernateDAO> productHibernateDAOProvider)
    {
        this.customerHibernateDAO = customerHibernateDAOProvider.get();
        this.taxDetailsHibernateDAO = taxDetailsHibernateDAOProvider.get();
        this.addressHibernateDAO = addressHibernateDAOProvider.get();
        this.cardDetailHibernateDAO = cardDetailHibernateDAOProvider.get();
        this.productHibernateDAO = productHibernateDAOProvider.get();
    }
}
