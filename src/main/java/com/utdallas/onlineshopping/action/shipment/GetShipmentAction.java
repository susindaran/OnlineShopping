package com.utdallas.onlineshopping.action.shipment;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.ShipmentHibernateDAO;
import com.utdallas.onlineshopping.models.Shipment;
import com.utdallas.onlineshopping.payload.response.shipment.AllShipmentsResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import com.utdallas.onlineshopping.validators.shipment.ShipmentsValidator;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prathyusha on 3/18/17.
 */
public class GetShipmentAction implements Action<AllShipmentsResponse>
{
    private HibernateUtil hibernateUtil;
    private ModelMapper modelMapper;
    private String requestURL;
    private int page, size;
    private String status;

    @Inject
    public GetShipmentAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    public GetShipmentAction withRequestURL(String requestURL)
    {
        this.requestURL = requestURL;
        return this;
    }

    public GetShipmentAction withPaginateDetails(int page, int size)
    {
        this.page = page;
        this.size = size;
        return this;
    }

    private String createLink(int page, int size)
    {
        return requestURL + "?page=" + page + "&size=" + size;
    }

    private Map<String, String> preparePaginationLinks(Long totalCount)
    {
        Map<String, String> linksMap = new HashMap<>();
        int pages = Math.toIntExact((totalCount / this.size) + (totalCount % this.size == 0 ? 0 : 1));
        linksMap.put("first", createLink(1, this.size));
        linksMap.put("prev", this.page > 1 ? createLink(this.page - 1, this.size) : null);
        linksMap.put("next", this.page * this.size < totalCount ? createLink( this.page + 1, this.size ) : null);
        linksMap.put("last", createLink(pages, size));

        return linksMap;
    }

    @Override
    public AllShipmentsResponse invoke()
    {
        ShipmentsValidator.validateQueryParams(page, size);
        ShipmentHibernateDAO shipmentHibernateDAO = this.hibernateUtil.getShipmentHibernateDAO();
        List<Shipment> shipmentList = shipmentHibernateDAO.getAll(page,size,status);
        Long totalCount = shipmentHibernateDAO.count();
        int count = shipmentList.size();

        AllShipmentsResponse allShipmentsResponse = AllShipmentsResponse.builder()
                .shipments(shipmentList)
                .count(count)
                .totalCount(totalCount)
                .links( preparePaginationLinks( totalCount ))
                .build();

        return allShipmentsResponse;
    }
}
