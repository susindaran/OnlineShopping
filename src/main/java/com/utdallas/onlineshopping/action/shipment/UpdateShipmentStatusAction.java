package com.utdallas.onlineshopping.action.shipment;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.ShipmentHibernateDAO;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.models.Product;
import com.utdallas.onlineshopping.models.Shipment;
import com.utdallas.onlineshopping.payload.request.shipment.ShipmentRequest;
import com.utdallas.onlineshopping.payload.response.shipment.AllShipmentsResponse;
import com.utdallas.onlineshopping.payload.response.shipment.ShipmentResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.joda.time.LocalDateTime;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UpdateShipmentStatusAction implements Action<AllShipmentsResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private ShipmentRequest shipmentRequest;
    private int shipmentId;
    private int page, size;
    private String status;

    public UpdateShipmentStatusAction withRequest(ShipmentRequest shipmentRequest)
    {
        this.shipmentRequest = shipmentRequest;
        return this;
    }

    public UpdateShipmentStatusAction withPaginateDetails(int page, int size)
    {
        this.page = page;
        this.size = size;
        return this;
    }

    @Inject
    public UpdateShipmentStatusAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public AllShipmentsResponse invoke()
    {
        ShipmentHibernateDAO shipmentHibernateDAO = hibernateUtil.getShipmentHibernateDAO();
        List<Shipment> shipments = shipmentHibernateDAO.getAllShipments(page,size);
        Shipment newShipment;
        List<ShipmentResponse> listOfShip=new ArrayList<ShipmentResponse>();
int id=0;
System.out.println("SIZE IS :"+shipments.size());
        try {
            for (Integer shipmentId : shipmentRequest.getShipmentId())
            {
                for(int i=0;i<shipments.size();i++)
                {
                    if(shipments.get(i).getShipmentId().intValue()==shipmentId)
                        id=i;
                }
                if (!Strings.isNullOrEmpty(shipmentRequest.getStatus()) && shipments.get(id).getStatus().equals("pick") && shipmentRequest.getStatus().equals("pack"))
                {

                    shipments.get(id).setStatus(shipmentRequest.getStatus());
                }

                if (!Strings.isNullOrEmpty(shipmentRequest.getStatus()) && shipments.get(id).getStatus().equals("pack") && shipmentRequest.getStatus().equals("ship"))
                {

                    shipments.get(id).setStatus(shipmentRequest.getStatus());
                }

                shipments.get(id).setUpdatedAt(LocalDateTime.now());
                newShipment=shipmentHibernateDAO.update(shipments.get(id));
                listOfShip = shipments.stream().map(shipment -> modelMapper.map(shipment, ShipmentResponse.class)).collect(Collectors.toList());
            }
            AllShipmentsResponse allShipmentsResponse = new AllShipmentsResponse();
            allShipmentsResponse.setShipmentResponses(listOfShip);
            return allShipmentsResponse;

        }

        catch( HibernateException e )
        {
            log.error(String.valueOf(e.getCause()));
            throw new InternalErrorException(e.getMessage());
        }

    }
}
