package com.utdallas.onlineshopping.action.orderDetail;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.OrderDetailHibernateDAO;
import com.utdallas.onlineshopping.enumerations.OrderStatus;
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


    public UpdateOrderDetailAction withRequest(GetOrderDetailRequest orderDetailRequest)
    {
        this.orderDetailRequest = orderDetailRequest;
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
        List<OrderDetail> orderDetails = orderDetailHibernateDAO.getOrderDetailsByIds(orderDetailRequest.getOrderDetailId());
        OrderDetail newOrderDetails;
        List<OrderDetailResponse> listOfOrderDetails=new ArrayList<OrderDetailResponse>();
        try{
            for(int i=0;i<orderDetails.size();i++)
            {

                if (orderDetails.get(i).getOrderDetailStatus().equals(OrderStatus.PENDING.getStatus()) && orderDetailRequest.getOrder_detail_status()==OrderStatus.SHIPPED)
                {

                    orderDetails.get(i).setOrderDetailStatus(orderDetailRequest.getOrder_detail_status().getStatus());
                }

                if (orderDetails.get(i).getOrderDetailStatus().equals(OrderStatus.SHIPPED.getStatus()) && orderDetailRequest.getOrder_detail_status()==OrderStatus.INVOICED)
                {

                    orderDetails.get(i).setOrderDetailStatus(orderDetailRequest.getOrder_detail_status().getStatus());
                }

                if (orderDetails.get(i).getOrderDetailStatus().equals(OrderStatus.RETURN_INITIATED.getStatus()) && orderDetailRequest.getOrder_detail_status()==OrderStatus.RETURN_RECEIVED)
                {

                    orderDetails.get(i).setOrderDetailStatus(orderDetailRequest.getOrder_detail_status().getStatus());
                }




                newOrderDetails=orderDetailHibernateDAO.update(orderDetails.get(i));
                listOfOrderDetails.add(modelMapper.map(newOrderDetails,OrderDetailResponse.class));
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
