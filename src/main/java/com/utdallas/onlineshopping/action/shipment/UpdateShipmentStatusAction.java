package com.utdallas.onlineshopping.action.shipment;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.ShipmentHibernateDAO;
import com.utdallas.onlineshopping.enumerations.ShipmentStatus;
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
        List<Shipment> shipments = shipmentHibernateDAO.getShipmentsByIds(shipmentRequest.getShipmentIds());
        List<ShipmentResponse> shipmentResponses = new ArrayList<>();

        try
        {
	        shipments.forEach( shipment ->
            {

                if( shipmentRequest.getStatus() == ShipmentStatus.PACKED  && shipment.getStatus().equals( ShipmentStatus.PICKED.getStatus() ) )
                {

                    shipment.setStatus( shipmentRequest.getStatus().getStatus() );
                }
                else if( shipmentRequest.getStatus() == ShipmentStatus.SHIPPED && shipment.getStatus().equals( ShipmentStatus.PACKED.getStatus() ) )
                {

                    shipment.setStatus( shipmentRequest.getStatus().getStatus() );
                }

                shipment.setUpdatedAt( LocalDateTime.now() );
                shipmentResponses.add( modelMapper.map( shipmentHibernateDAO.update( shipment ), ShipmentResponse.class ) );
            });
            AllShipmentsResponse allShipmentsResponse = new AllShipmentsResponse();
            allShipmentsResponse.setShipments(shipmentResponses );
            return allShipmentsResponse;
        }
        catch( HibernateException e )
        {
            log.error(String.valueOf(e.getCause()));
            throw new InternalErrorException(e.getMessage());
        }
    }
}
