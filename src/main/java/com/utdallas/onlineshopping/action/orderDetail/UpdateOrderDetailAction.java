package com.utdallas.onlineshopping.action.orderDetail;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.OrderDetailHibernateDAO;
import com.utdallas.onlineshopping.exceptions.InternalErrorException;
import com.utdallas.onlineshopping.models.OrderDetail;
import com.utdallas.onlineshopping.payload.request.orderDetail.GetOrderDetailRequest;
import com.utdallas.onlineshopping.payload.response.orderdetail.GetAllOrderDetailsResponse;
import com.utdallas.onlineshopping.payload.response.orderdetail.OrderDetailResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class UpdateOrderDetailAction implements Action<GetAllOrderDetailsResponse>
    {
        private final HibernateUtil hibernateUtil;
        private ModelMapper modelMapper;
        private GetOrderDetailRequest orderDetailRequest;
        private int orderDetailId;
        private int page, size;
        private String status;

    public UpdateOrderDetailAction withRequest(GetOrderDetailRequest orderDetailRequest)
    {
        this.orderDetailRequest = orderDetailRequest;
        return this;
    }

    public UpdateOrderDetailAction withPaginateDetails(int page, int size)
    {
        this.page = page;
        this.size = size;
        return this;
    }

    @Inject
    public UpdateOrderDetailAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
    {
        this.hibernateUtil = hibernateUtilProvider.get();
        this.modelMapper = modelMapper;
    }

    @Override
    public GetAllOrderDetailsResponse invoke()
    {
        OrderDetailHibernateDAO orderDetailHibernateDAO = hibernateUtil.getOrderDetailHibernateDAO();
        List<OrderDetail> orderDetails = orderDetailHibernateDAO.getAllOrderDetails(page,size);
        OrderDetail newOrderDetails;
        List<OrderDetailResponse> listOfOrderDetails=new ArrayList<OrderDetailResponse>();
        int id=0;

        try {
            for (Integer orderdetailId : orderDetailRequest.getOrderDetailId())
            {
                for(int i=0;i<orderDetails.size();i++)
                {
                    if(orderDetails.get(i).getOrderDetailId().intValue()==orderdetailId)
                        id=i;
                }
                if (!Strings.isNullOrEmpty(orderDetailRequest.getOrder_detail_status()) && orderDetails.get(id).getOrderDetailStatus().equals("pending") && orderDetailRequest.getOrder_detail_status().equals("shipped"))
                {

                    orderDetails.get(id).setOrderDetailStatus(orderDetailRequest.getOrder_detail_status());
                }

                if (!Strings.isNullOrEmpty(orderDetailRequest.getOrder_detail_status()) && orderDetails.get(id).getOrderDetailStatus().equals("shipped") && orderDetailRequest.getOrder_detail_status().equals("invoiced"))
                {

                    orderDetails.get(id).setOrderDetailStatus(orderDetailRequest.getOrder_detail_status());
                }
                if (!Strings.isNullOrEmpty(orderDetailRequest.getOrder_detail_status()) && orderDetails.get(id).getOrderDetailStatus().equals("return initiated") && orderDetailRequest.getOrder_detail_status().equals("return received"))
                {

                    orderDetails.get(id).setOrderDetailStatus(orderDetailRequest.getOrder_detail_status());
                }


                newOrderDetails=orderDetailHibernateDAO.update(orderDetails.get(id));
                listOfOrderDetails = orderDetails.stream().map(shipment -> modelMapper.map(shipment, OrderDetailResponse.class)).collect(Collectors.toList());
            }
             GetAllOrderDetailsResponse getAllOrderDetailsResponse= new GetAllOrderDetailsResponse();
            getAllOrderDetailsResponse.setOrderDetailsResponses(listOfOrderDetails);
            return getAllOrderDetailsResponse;

        }

        catch( HibernateException e )
        {

            log.error(String.valueOf(e.getCause()));
            throw new InternalErrorException(e.getMessage());
        }


    }
}
