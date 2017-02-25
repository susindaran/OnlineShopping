package com.utdallas.onlineshopping.action.product;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.ProductHibernateDAO;
import com.utdallas.onlineshopping.exceptions.ConflictingRequestException;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.exceptions.NotFoundException;
import com.utdallas.onlineshopping.models.Product;
import com.utdallas.onlineshopping.payload.request.product.UpdateProductRequest;
import com.utdallas.onlineshopping.payload.response.product.ProductResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;

import java.util.Collections;

@Slf4j
public class UpdateProductAction implements Action<ProductResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private UpdateProductRequest updateProductRequest;
    private String product_id;

    public UpdateProductAction withRequest(UpdateProductRequest updateProductRequest)
    {
        this.updateProductRequest = updateProductRequest;
        return this;
    }

    public UpdateProductAction withId(String id)
    {
        this.product_id = id;
        return this;
    }

    @Inject
    public UpdateProductAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductResponse invoke()
    {
        ProductHibernateDAO productHibernateDAO = hibernateUtil.getProductHibernateDAO();
        Product product = productHibernateDAO.findByParams(Collections.singletonMap("productId", product_id)).get(0);

        try
        {
            if( !Strings.isNullOrEmpty(updateProductRequest.getProductName()) )
                product.setProductName(updateProductRequest.getProductName());
            if( !Strings.isNullOrEmpty(updateProductRequest.getProductDescription()) )
                product.setDescription(updateProductRequest.getProductDescription());

            if(updateProductRequest.getQuantity()==-1){
                product.setQuantity(0);
            }
            if(updateProductRequest.getPrice()==-1){
                product.setPrice(0.0);
            }

            Product newProduct = productHibernateDAO.update(product);
            return modelMapper.map(newProduct, ProductResponse.class);
        }
        catch( HibernateException e )
        {
            log.error(String.valueOf(e.getCause()));
            throw new InternalErrorException(e.getMessage());
        }
    }
}
