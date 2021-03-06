package com.betadevels.onlineshopping.action.shipment;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.betadevels.onlineshopping.action.Action;
import com.betadevels.onlineshopping.db.hibernate.ShipmentHibernateDAO;
import com.betadevels.onlineshopping.models.Shipment;
import com.betadevels.onlineshopping.payload.response.shipment.AllShipmentsResponse;
import com.betadevels.onlineshopping.payload.response.shipment.ShipmentResponse;
import com.betadevels.onlineshopping.util.HibernateUtil;
import com.betadevels.onlineshopping.validators.shipment.ShipmentsValidator;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.stream.Collectors;

import static com.betadevels.onlineshopping.util.Utility.preparePaginationLinks;

public class GetAllShipmentsAction implements Action<AllShipmentsResponse>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String requestURL;
    private int page, size;
    private String status;

    @Inject
    public GetAllShipmentsAction( Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper )
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public GetAllShipmentsAction withRequestURL( String requestURL )
    {
        this.requestURL = requestURL;
        return this;
    }

    public GetAllShipmentsAction withPaginateDetails( int page, int size )
    {
        this.page = page;
        this.size = size;
        return this;
    }

    public GetAllShipmentsAction withStatus(String status)
    {
        this.status = status;
        return this;
    }

    @Override
    public AllShipmentsResponse invoke()
    {
        ShipmentsValidator.validatePaginateParameters(page, size);
        ShipmentHibernateDAO shipmentHibernateDAO = this.hibernateUtil.getShipmentHibernateDAO();
        List<Shipment> shipmentList = shipmentHibernateDAO.getAll(page,size,status);
        Long totalCount = shipmentHibernateDAO.countWithStatus( status );
        int count = shipmentList.size();
        List<ShipmentResponse> shipmentResponses = shipmentList.stream().map(shipment -> modelMapper.map(shipment, ShipmentResponse.class)).collect(Collectors.toList());

        AllShipmentsResponse allShipmentsResponse = new AllShipmentsResponse();
        allShipmentsResponse.setShipments( shipmentResponses );
        allShipmentsResponse.setCount(count);
        allShipmentsResponse.setTotalCount(totalCount);
        allShipmentsResponse.setLinks(preparePaginationLinks(totalCount, size, page, requestURL));
        return allShipmentsResponse;
    }
}

