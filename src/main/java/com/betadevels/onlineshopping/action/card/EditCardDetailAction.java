package com.betadevels.onlineshopping.action.card;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.CardDetailHibernateDAO;
import com.betadevels.onlineshopping.exceptions.NotFoundException;
import com.betadevels.onlineshopping.models.CardDetail;
import com.betadevels.onlineshopping.payload.request.card.CardDetailRequest;
import com.betadevels.onlineshopping.payload.response.card.CardDetailResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.betadevels.onlineshopping.util.Utility;
import org.modelmapper.ModelMapper;

public class EditCardDetailAction implements Action<CardDetailResponse>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String cardNumber;
    private CardDetailRequest cardDetailRequest;

    @Inject
    public EditCardDetailAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public EditCardDetailAction forCardNumber( String cardNumber )
    {
        this.cardNumber = cardNumber;
        return this;
    }

    public EditCardDetailAction withRequest( CardDetailRequest cardDetailRequest )
    {
        this.cardDetailRequest = cardDetailRequest;
        return this;
    }

    @Override
    public CardDetailResponse invoke()
    {
        CardDetailHibernateDAO cardDetailHibernateDAO = this.hibernateUtil.getCardDetailHibernateDAO();
        Optional<CardDetail> cardDetailOptional = cardDetailHibernateDAO.findByCardNumber(cardNumber);

        if( cardDetailOptional.isPresent() )
        {
            CardDetail cardDetail = cardDetailOptional.get();
            if( !Strings.isNullOrEmpty( cardDetailRequest.getCardNumber() ) )
                cardDetail.setCardNumber( cardDetailRequest.getCardNumber() );
            if( !Strings.isNullOrEmpty( cardDetailRequest.getExpiryDate() ) )
                cardDetail.setExpiryDate(Utility.StringToDate( cardDetailRequest.getExpiryDate() ));
            if( !Strings.isNullOrEmpty( cardDetailRequest.getNameOnCard() ) )
                cardDetail.setNameOnCard( cardDetailRequest.getNameOnCard() );

            CardDetail newCardDetail = cardDetailHibernateDAO.update(cardDetail);

            return modelMapper.map( newCardDetail, CardDetailResponse.class);
        }
        else
        {
            throw new NotFoundException("No Card Detail matching the given Card Number");
        }

    }
}
