package com.betadevels.onlineshopping.action.product;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.betadevels.onlineshopping.models.Product;
import com.betadevels.onlineshopping.payload.request.product.ProductRequest;
import com.betadevels.onlineshopping.payload.response.product.ProductResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

public class AddProductAction implements Action<ProductResponse>
{
    HibernateUtil hibernateUtil;
    ModelMapper modelMapper;
    private ProductRequest productRequest;

    public AddProductAction withRequest(ProductRequest productRequest)
    {
        this.productRequest = productRequest;
        return this;

    }

    @Inject
    public AddProductAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductResponse invoke()
    {
        ProductHibernateDAO productHibernateDAO = this.hibernateUtil.getProductHibernateDAO();

        //Configured modelMapper to map the productId of Product from the categoryPrefix
        //of the ProductRequest
        Product product = modelMapper.map(productRequest, Product.class);
        Product newProduct = productHibernateDAO.create(product);

        //Appending the ID to the product_id column
        newProduct.setProductId( String.format("%s%010d", productRequest.getCategoryPrefix(), newProduct.getId()) );
        productHibernateDAO.update( newProduct );

        return modelMapper.map(newProduct, ProductResponse.class);
    }
}
