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
    private final CategoryHibernateDAO categoryHibernateDAO;
    private final OrderHibernateDAO orderHibernateDAO;
    private final ShipmentHibernateDAO shipmentHibernateDAO;
    private final OrderDetailHibernateDAO orderDetailHibernateDAO;
    private final CartHibernateDAO cartHibernateDAO;
    private final OfferHibernateDAO offerHibernateDAO;
    private final PaymentHibernateDAO paymentHibernateDAO;

    @Inject
    public HibernateUtil(Provider<CustomerHibernateDAO> customerHibernateDAOProvider,
                         Provider<TaxDetailsHibernateDAO> taxDetailsHibernateDAOProvider,
                         Provider<AddressHibernateDAO> addressHibernateDAOProvider,
                         Provider<CardDetailHibernateDAO> cardDetailHibernateDAOProvider,
                         Provider<ProductHibernateDAO> productHibernateDAOProvider,
                         Provider<CategoryHibernateDAO> categoryHibernateDAOProvider,
                         Provider<OrderHibernateDAO> orderHibernateDAOProvider,
                         Provider<ShipmentHibernateDAO> shipmentHibernateDAOProvider,
                         Provider<OrderDetailHibernateDAO> orderDetailHibernateDAOProvider,
                         Provider<CartHibernateDAO> cartHibernateDAOProvider,
                         Provider<OfferHibernateDAO> offerHibernateDAOProvider,
                         Provider<PaymentHibernateDAO> paymentHibernateDAOProvider)
    {
        this.customerHibernateDAO = customerHibernateDAOProvider.get();
        this.taxDetailsHibernateDAO = taxDetailsHibernateDAOProvider.get();
        this.addressHibernateDAO = addressHibernateDAOProvider.get();
        this.cardDetailHibernateDAO = cardDetailHibernateDAOProvider.get();
        this.productHibernateDAO = productHibernateDAOProvider.get();
        this.categoryHibernateDAO = categoryHibernateDAOProvider.get();
        this.orderHibernateDAO = orderHibernateDAOProvider.get();
        this.shipmentHibernateDAO = shipmentHibernateDAOProvider.get();
        this.orderDetailHibernateDAO = orderDetailHibernateDAOProvider.get();
        this.cartHibernateDAO = cartHibernateDAOProvider.get();
        this.offerHibernateDAO = offerHibernateDAOProvider.get();
        this.paymentHibernateDAO = paymentHibernateDAOProvider.get();
    }
}
