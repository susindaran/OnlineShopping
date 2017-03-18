package com.utdallas.onlineshopping.action.cart;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CartHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.utdallas.onlineshopping.exceptions.BadRequestException;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Cart;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.models.Product;
import com.utdallas.onlineshopping.payload.request.cart.AddProductToCartRequest;
import com.utdallas.onlineshopping.payload.response.cart.CartResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


public class AddProductToCartAction implements Action<CartResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private AddProductToCartRequest request;

    @Inject
    public AddProductToCartAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public AddProductToCartAction withRequest(AddProductToCartRequest request )
    {
        this.request = request;
        return this;
    }

    @Override
    public CartResponse invoke()
    {
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();
        ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();
        CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();

        Optional<Customer> customerOptional = customerHibernateDAO.findById(request.getCustomerId());
        Optional<Product> productOptional = productHibernateDAO.findById(request.getProductId());

        if( customerOptional.isPresent() && productOptional.isPresent() )
        {
            Customer customer = customerOptional.get();
            Product product = productOptional.get();

            if( product.getQuantity() < request.getQuantity() )
            {
                throw new BadRequestException("Requested quantity exceeds availability");
            }

            Cart cart = Cart.builder().customer( customer )
                    .product( product )
                    .quantity( request.getQuantity() )
                    .build();

            Cart newCart = cartHibernateDAO.create(cart);

            //Updating the new available quantity of the product
            product.setQuantity( product.getQuantity() - request.getQuantity() );
            productHibernateDAO.update( product );

            CartResponse cartResponse = modelMapper.map(newCart, CartResponse.class);
            cartResponse.setCustomerId( newCart.getCustomer().getCustomerId() );
            return cartResponse;
        }

        List<String> errors = new ArrayList<>();
        if( !customerOptional.isPresent() )
        {
            errors.add("No customer matching the given customer_id");
        }
        if( !productOptional.isPresent() )
        {
            errors.add("No product matching the given product_id");
        }
        throw new NotFoundException(errors);
    }
}
