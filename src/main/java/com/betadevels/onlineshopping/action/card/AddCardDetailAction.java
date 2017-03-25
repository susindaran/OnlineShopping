package com.betadevels.onlineshopping.action.card;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CardDetailHibernateDAO;
import com.betadevels.onlineshopping.db.hibernate.CustomerHibernateDAO;
import com.betadevels.onlineshopping.models.CardDetail;
import com.betadevels.onlineshopping.models.Customer;
import com.betadevels.onlineshopping.payload.request.card.CardDetailRequest;
import com.betadevels.onlineshopping.payload.response.card.CardDetailResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.betadevels.onlineshopping.util.Utility;
import org.modelmapper.ModelMapper;

public class AddCardDetailAction implements Action<CardDetailResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private CardDetailRequest cardDetailRequest;
    private Long customerId;

    @Inject
    public AddCardDetailAction(Provider<HibernateUtil> hibernateUtilProvider,
                               ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public AddCardDetailAction withRequest(CardDetailRequest cardDetailRequest)
    {
        this.cardDetailRequest = cardDetailRequest;
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

        CardDetail cardDetail = modelMapper.map(cardDetailRequest, CardDetail.class);
        cardDetail.setExpiryDate(Utility.StringToDate( cardDetailRequest.getExpiryDate() ));
        cardDetail.setCustomer(customer);

        CardDetail newCardDetails = cardDetailHibernateDAO.create( cardDetail );

        CardDetailResponse cardDetailResponse = modelMapper.map(newCardDetails, CardDetailResponse.class);
        cardDetailResponse.setCustomerId(customerId);

        return cardDetailResponse;
    }
}
