package com.betadevels.onlineshopping.action.cart;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.Cart;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.request.cart.DeleteItemsInCartRequest;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by girijagodbole on 3/28/17.
 */
public class DeleteItemsInCartAction implements Action<Void> {
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private int cartId;
    private Long customerId;
    DeleteItemsInCartRequest request;

    @Inject
    public DeleteItemsInCartAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public DeleteItemsInCartAction withCartId(int cartId )
    {
        this.cartId = cartId;
        return this;
    }
    public DeleteItemsInCartAction forCustomerId(Long customerId)
    {
        this.customerId = customerId;
        return this;
    }

    @Override
    public Void invoke() {
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();
        CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();
        ProductHibernateDAO productHibernateDAO=this.hibernateUtil.getProductHibernateDAO();
        List<Cart> cartList= cartHibernateDAO.findByParams(Collections.singletonMap("cartId", this.cartId));

        Cart cart1 =  cartList.get( 0 );
        String prod_id= cart1.getProduct().getProductId();
        Long cust_id= cart1.getCustomer().getCustomerId();
        Optional<Product> productOptional = productHibernateDAO.findById(prod_id);
        Optional<Customer> customerOptional = customerHibernateDAO.findById(cust_id);

        cartHibernateDAO.deleteByCartId(cartId);

        if(customerOptional.isPresent() && productOptional.isPresent() )
        {

            Product product = productOptional.get();
            Customer customer= customerOptional.get();



            //Adding the quantity to product which the customer deleted from cart
            product.setQuantity( product.getQuantity() + request.getQuantity() );
            productHibernateDAO.update( product );


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

