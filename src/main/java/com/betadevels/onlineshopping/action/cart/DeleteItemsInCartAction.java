package com.betadevels.onlineshopping.action.cart;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CartHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.models.Cart;
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
    private Long cartId;

    DeleteItemsInCartRequest request;

    @Inject
    public DeleteItemsInCartAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public DeleteItemsInCartAction withCartId(Long cartId )
    {
        this.cartId = cartId;
        return this;
    }

    @Override
    public Void invoke() {
        CartHibernateDAO cartHibernateDAO = this.hibernateUtil.getCartHibernateDAO();
        ProductHibernateDAO productHibernateDAO=this.hibernateUtil.getProductHibernateDAO();
        Optional<Cart> cartList= cartHibernateDAO.findById(cartId);

        Cart cart =  cartList.get( );
        Product product = cart.getProduct();
        try {

            cartHibernateDAO.delete(cart);

                //Adding the quantity to product which the customer deleted from cart
                product.setQuantity(product.getQuantity() + cart.getQuantity());
                productHibernateDAO.update(product);


            }
        catch(NullPointerException e){
            e.printStackTrace();
        }
        return null;

    }
}

