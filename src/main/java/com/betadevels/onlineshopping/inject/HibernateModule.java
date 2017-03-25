package com.betadevels.onlineshopping.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.betadevels.onlineshopping.configurations.OnlineShoppingConfiguration;
import com.betadevels.onlineshopping.db.GenericDAO;
import com.betadevels.onlineshopping.db.hibernate.*;
import com.betadevels.onlineshopping.models.*;
import io.dropwizard.hibernate.HibernateBundle;

public class HibernateModule extends AbstractModule
{
    public static final Class[] ENTITIES = new Class[]
            {
                    Customer.class,
                    TaxDetails.class,
                    Address.class,
                    CardDetail.class,
                    Product.class,
                    Order.class,
                    Shipment.class,
                    OrderDetail.class,
                    Cart.class,
                    Offer.class,
                    Payment.class
            };

    private final HibernateBundle<OnlineShoppingConfiguration> hibernateBundle;

    public HibernateModule(HibernateBundle<OnlineShoppingConfiguration> hibernateBundle)
    {
        this.hibernateBundle = hibernateBundle;
    }

    @Override
    protected void configure()
    {
        bind(new TypeLiteral<GenericDAO<Customer>>(){}).to(CustomerHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<TaxDetails>>(){}).to(TaxDetailsHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<Address>>(){}).to(AddressHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<CardDetail>>(){}).to(CardDetailHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<Product>>(){}).to(ProductHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<Category>>(){}).to(CategoryHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<Order>>(){}).to(OrderHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<Shipment>>(){}).to(ShipmentHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<OrderDetail>>(){}).to(OrderDetailHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<Cart>>(){}).to(CartHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<Offer>>(){}).to(OfferHibernateDAO.class);
        bind(new TypeLiteral<GenericDAO<Payment>>(){}).to(PaymentHibernateDAO.class);
    }

    @Provides
    public CustomerHibernateDAO provideCustomerHibernateDAO()
    {
        return new CustomerHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public TaxDetailsHibernateDAO provideTaxDetailsHibernateDAO()
    {
        return new TaxDetailsHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public AddressHibernateDAO provideAddressHibernateDAO()
    {
        return new AddressHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public CardDetailHibernateDAO provideCardDetailHibernateDAO()
    {
        return new CardDetailHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public ProductHibernateDAO provideProductHibernateDAO()
    {
        return new ProductHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public CategoryHibernateDAO provideCategoryHibernateDAO()
    {
        return new CategoryHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public OrderHibernateDAO provideOrderHibernateDAO()
    {
        return new OrderHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public ShipmentHibernateDAO provideShipmentHibernateDAO()
    {
        return new ShipmentHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public OrderDetailHibernateDAO provideOrderDetailHibernateDAO()
    {
        return new OrderDetailHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public CartHibernateDAO provideCartHibernateDAO()
    {
        return new CartHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public OfferHibernateDAO provideOfferHibernateDAO()
    {
        return new OfferHibernateDAO(hibernateBundle.getSessionFactory());
    }

    @Provides
    public PaymentHibernateDAO providePaymentHibernateDAO()
    {
        return new PaymentHibernateDAO(hibernateBundle.getSessionFactory());
    }
}
