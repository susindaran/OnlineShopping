package com.utdallas.onlineshopping.action.card;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.CardDetailHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.utdallas.onlineshopping.models.CardDetail;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.payload.request.card.AddCardDetailRequest;
import com.utdallas.onlineshopping.payload.response.card.CardDetailResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import org.modelmapper.ModelMapper;

/**
 * Created by susindaran on 2/17/17.
 */
public class AddCardDetailAction implements Action<CardDetailResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private AddCardDetailRequest addCardDetailRequest;
    private Long customerId;

    @Inject
    public AddCardDetailAction(Provider<HibernateUtil> hibernateUtilProvider,
                               ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public AddCardDetailAction withRequest(AddCardDetailRequest addCardDetailRequest)
    {
        this.addCardDetailRequest = addCardDetailRequest;
        return this;
    }

    public AddCardDetailAction forCustomerId(Long customerId)
    {
        this.customerId = customerId;
        return this;
    }

    @Override
    public CardDetailResponse invoke()
    {
        CustomerHibernateDAO customerHibernateDAO = this.hibernateUtil.getCustomerHibernateDAO();
        CardDetailHibernateDAO cardDetailHibernateDAO = this.hibernateUtil.getCardDetailHibernateDAO();

        //TODO: Validity check and corresponding exception throwing
        Customer customer = customerHibernateDAO.findById(customerId).get();

        CardDetail cardDetail = modelMapper.map(addCardDetailRequest, CardDetail.class);
        cardDetail.setCustomer(customer);

        CardDetail newCardDetails = cardDetailHibernateDAO.create( cardDetail );

        CardDetailResponse cardDetailResponse = modelMapper.map(newCardDetails, CardDetailResponse.class);
        cardDetailResponse.setCustomerId(customerId);

        return cardDetailResponse;
    }
}
