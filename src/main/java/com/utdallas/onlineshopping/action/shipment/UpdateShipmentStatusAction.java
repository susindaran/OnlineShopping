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
import com.utdallas.onlineshopping.payload.response.shipment.ShipmentResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.joda.time.LocalDateTime;
import org.modelmapper.ModelMapper;

import java.util.Collections;

@Slf4j
public class UpdateShipmentStatusAction implements Action<ShipmentResponse>
{
    private final HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private ShipmentRequest shipmentRequest;
    private Long shipmentId;

    public UpdateShipmentStatusAction withRequest(ShipmentRequest shipmentRequest)
    {
        this.shipmentRequest = shipmentRequest;
        return this;
    }

    public UpdateShipmentStatusAction withId(Long id)
    {
        this.shipmentId = id;
        return this;
    }

    @Inject
    public UpdateShipmentStatusAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public ShipmentResponse invoke()
    {
        ShipmentHibernateDAO shipmentHibernateDAO = hibernateUtil.getShipmentHibernateDAO();
        Shipment shipment = shipmentHibernateDAO.findByParams(Collections.singletonMap("shipmentId", shipmentId)).get(0);

        try
        {
            if( !Strings.isNullOrEmpty(shipmentRequest.getStatus()) )
                shipment.setStatus(shipmentRequest.getStatus());

                shipment.setUpdatedAt(LocalDateTime.now());


            Shipment newShipment = shipmentHibernateDAO.update(shipment);
            return modelMapper.map(newShipment, ShipmentResponse.class);
        }
        catch( HibernateException e )
        {
            log.error(String.valueOf(e.getCause()));
            throw new InternalErrorException(e.getMessage());
        }
    }
}
