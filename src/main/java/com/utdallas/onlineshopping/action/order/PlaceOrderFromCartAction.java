package com.utdallas.onlineshopping.action.order;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.*;
import com.utdallas.onlineshopping.enumerations.OrderStatus;
import com.utdallas.onlineshopping.enumerations.ShipmentStatus;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.*;
import com.utdallas.onlineshopping.payload.request.order.PlaceOrderRequest;
import com.utdallas.onlineshopping.payload.response.order.OrderResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.joda.time.LocalDateTime;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceOrderFromCartAction implements Action<OrderResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private PlaceOrderRequest request;
    private Long customerId;

    @Inject
    public PlaceOrderFromCartAction(Provider<HibernateUtil> hibernateUtilProvider,
                                    ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public PlaceOrderFromCartAction withRequest(PlaceOrderRequest request)
    {
        this.request = request;
        return this;
    }

    public PlaceOrderFromCartAction forCustomerId(Long customerId)
    {
        this.customerId = customerId;
        return this;
    }

    @Override
    public OrderResponse invoke()
    {
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();
        AddressHibernateDAO addressHibernateDAO = this.hibernateUtil.getAddressHibernateDAO();
        OrderHibernateDAO orderHibernateDAO = this.hibernateUtil.getOrderHibernateDAO();
        ShipmentHibernateDAO shipmentHibernateDAO = this.hibernateUtil.getShipmentHibernateDAO();
        CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();
        OrderDetailHibernateDAO orderDetailHibernateDAO = this.hibernateUtil.getOrderDetailHibernateDAO();

        //Get customer
        Optional<Customer> customerOptional = customerHibernateDAO.findById(customerId);

        //Get addresses
        Optional<Address> billingAddressOptional = addressHibernateDAO.findByIdForCustomer(request.getBillingAddressId(), customerId);
        Optional<Address> shippingAddressOptional = addressHibernateDAO.findByIdForCustomer(request.getShippingAddressId(), customerId);

        //Validate customer and address IDs
        if( customerOptional.isPresent() && billingAddressOptional.isPresent() && shippingAddressOptional.isPresent() )
        {
            Customer customer = customerOptional.get();
            Address billingAddress = billingAddressOptional.get();
            Address shippingAddress = shippingAddressOptional.get();

            //Create order
            Order order = orderHibernateDAO.create(Order.builder()
                    .customer(customer)
                    .shippingAddress(shippingAddress)
                    .billingAddress(billingAddress)
                    .orderStatus(OrderStatus.PENDING.getStatus())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now()).build());

            Shipment shipment = shipmentHibernateDAO.create(Shipment.builder()
                    .order(order)
                    .status(ShipmentStatus.PICKED.getStatus())
                    .deliveryDueDate(LocalDateTime.now().plusDays(10))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now()).build());

            List<Cart> cartItems = cartHibernateDAO.getCartItemsOfCustomer(customer);
            cartItems.forEach( cartItem -> orderDetailHibernateDAO.create( OrderDetail.builder()
                    .order( order )
                    .product( cartItem.getProduct() )
                    .shipment( shipment )
                    .quantity( cartItem.getQuantity() ).build() ));

            List<Long> cartIds = cartItems.stream().map(Cart::getCartId).collect(Collectors.toList());
            cartHibernateDAO.deleteByIDs( cartIds, "cartId");

            shipmentHibernateDAO.reloadShipment( shipment );
            orderHibernateDAO.reloadOrder( order );

            return modelMapper.map( order, OrderResponse.class );
        }

        List<String> errors = new ArrayList<>();
        if( !customerOptional.isPresent() )
            errors.add("No customer matching the given customer_id");
        if( !billingAddressOptional.isPresent() )
            errors.add("No Billing Address matching the given billing_address_id for the customer");
        if( !shippingAddressOptional.isPresent() )
            errors.add("No Shipping Address matching the given shipping_address_id for the customer");

        throw new NotFoundException(errors);
    }
}
