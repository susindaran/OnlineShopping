package com.utdallas.onlineshopping.action.shipment;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.OrderDetailHibernateDAO;
import com.utdallas.onlineshopping.db.hibernate.ShipmentHibernateDAO;
import com.utdallas.onlineshopping.enumerations.OrderStatus;
import com.utdallas.onlineshopping.enumerations.ShipmentStatus;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.models.Shipment;
import com.utdallas.onlineshopping.payload.request.shipment.UpdateShipmentsStatusRequest;
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
    private UpdateShipmentsStatusRequest updateShipmentsStatusRequest;

    public UpdateShipmentStatusAction withRequest(UpdateShipmentsStatusRequest updateShipmentsStatusRequest )
    {
        this.updateShipmentsStatusRequest = updateShipmentsStatusRequest;
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
	    OrderDetailHibernateDAO orderDetailHibernateDAO = hibernateUtil.getOrderDetailHibernateDAO();
	    List<Shipment> shipments = shipmentHibernateDAO.getShipmentsByIds( updateShipmentsStatusRequest.getShipmentIds() );
        List<ShipmentResponse> shipmentResponses = new ArrayList<>();

        try
        {
	        shipments.forEach( shipment ->
            {
                if( updateShipmentsStatusRequest.getStatus() == ShipmentStatus.PACKED  && shipment.getStatus().equals( ShipmentStatus.PICKED.getStatus() ) )
                {
                    shipment.setStatus( updateShipmentsStatusRequest.getStatus().getStatus() );
                }
                else if( updateShipmentsStatusRequest.getStatus() == ShipmentStatus.SHIPPED && shipment.getStatus().equals( ShipmentStatus.PACKED.getStatus() ) )
                {
                    shipment.getOrderDetails().forEach( orderDetail -> {
                    	orderDetail.setOrderDetailStatus( OrderStatus.SHIPPED.getStatus() );
                    	orderDetailHibernateDAO.update( orderDetail );
                    } );
                    shipment.setStatus( updateShipmentsStatusRequest.getStatus().getStatus() );
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
