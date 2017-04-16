package com.betadevels.onlineshopping.action.cart;

import com.betadevels.onlineshopping.db.hibernate.OfferHibernateDAO;
import com.betadevels.onlineshopping.models.Offer;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.exceptions.BadRequestException;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.request.cart.AddProductToCartRequest;
import com.betadevels.onlineshopping.payload.response.cart.AddProductToCartResponse;
import com.betadevels.onlineshopping.payload.response.cart.CartResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import java.util.*;

@Slf4j
public class AddProductToCartAction implements Action<AddProductToCartResponse>
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
    public AddProductToCartResponse invoke()
    {
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();
        ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();
        CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();
        OfferHibernateDAO offerHibernateDAO = this.hibernateUtil.getOfferHibernateDAO();

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




            Cart.CartBuilder cartBuilder = Cart.builder().customer( customer )
                                            .product( product )
                                            .quantity( request.getQuantity() );

            if( !Strings.isNullOrEmpty( request.getCouponId() ) )
            {
                Map<String, Object> searchParams = new HashMap<>();
                searchParams.put( "couponId", request.getCouponId() );
                searchParams.put( "product", product );
                List<Offer> offers = offerHibernateDAO.findByParams( searchParams );
                if( offers.size() < 1 )
                {
                    throw new NotFoundException( Collections.singletonList("Invalid coupon code") );
                }

                cartBuilder.offer( offers.get( 0 ) );
            }

            Cart cart = cartBuilder.build();
            try
            {
            Cart newCart = cartHibernateDAO.create(cart);

            //Updating the new available quantity of the product
            product.setQuantity( product.getQuantity() - request.getQuantity() );
            productHibernateDAO.update( product );

	        AddProductToCartResponse addProductToCartResponse = new AddProductToCartResponse();
	        addProductToCartResponse.setItemsInCart( cartHibernateDAO.countForCustomer( customer ) );
	        addProductToCartResponse.setProductAdded( modelMapper.map( newCart, CartResponse.class ) );

	        return addProductToCartResponse;
            }
            catch(HibernateException e)
            {
                log.error(String.valueOf(e.getCause()));
                throw new BadRequestException("Product already exists in cart !");

            }
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
