package com.utdallas.onlineshopping.action.shipment;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.ShipmentHibernateDAO;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
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
import java.util.List;

@Slf4j
public class UpdateShipmentStatusAction implements Action<AllShipmentsResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private ShipmentRequest shipmentRequest;

    public UpdateShipmentStatusAction withRequest(ShipmentRequest shipmentRequest)
    {
        this.shipmentRequest = shipmentRequest;
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
        List<Shipment> shipments = shipmentHibernateDAO.getShipmentsByIds(shipmentRequest.getShipmentId());
        Shipment newShipment;
        List<ShipmentResponse> listOfShip=new ArrayList<ShipmentResponse>();
        try {

                for(int i=0;i<shipments.size();i++) {

                    if (!Strings.isNullOrEmpty(shipmentRequest.getStatus()) && shipments.get(i).getStatus().equals("pick") && shipmentRequest.getStatus().equals("pack")) {

                        shipments.get(i).setStatus(shipmentRequest.getStatus());
                    }

                    if (!Strings.isNullOrEmpty(shipmentRequest.getStatus()) && shipments.get(i).getStatus().equals("pack") && shipmentRequest.getStatus().equals("ship")) {

                        shipments.get(i).setStatus(shipmentRequest.getStatus());
                    }

                    shipments.get(i).setUpdatedAt(LocalDateTime.now());
                    newShipment = shipmentHibernateDAO.update(shipments.get(i));
                    listOfShip.add(modelMapper.map(newShipment,ShipmentResponse.class));
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
