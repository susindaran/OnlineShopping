package com.utdallas.onlineshopping.action.orderdetail;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.OrderDetailHibernateDAO;
import com.utdallas.onlineshopping.models.OrderDetail;
import com.utdallas.onlineshopping.payload.response.orderdetail.AllOrderDetailsResponse;
import com.utdallas.onlineshopping.payload.response.orderdetail.OrderDetailResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import com.utdallas.onlineshopping.validators.orderDetail.OrderDetailValidator;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

import static com.utdallas.onlineshopping.util.Utility.preparePaginationLinks;

public class GetAllOrderDetailsActions implements Action<AllOrderDetailsResponse>
    {
        private HibernateUtil hibernateUtil;
        private ModelMapper modelMapper;
        private String requestURL;
        private int page, size;
        private String status;

    @Inject
    public GetAllOrderDetailsActions( Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper )
        {
            this.hibernateUtil = hibernateUtilProvider.get();
            this.modelMapper = modelMapper;
        }

    public GetAllOrderDetailsActions withRequestURL( String requestURL )
    {
        this.requestURL = requestURL;
        return this;
    }

    public GetAllOrderDetailsActions withPaginateDetails( int page, int size )
    {
        this.page = page;
        this.size = size;
        return this;
    }

    public GetAllOrderDetailsActions withStatus( String status )
    {
    	this.status = status;
    	return this;
    }

    @Override
    public AllOrderDetailsResponse invoke()
    {
        OrderDetailValidator.validateQueryParams(page, size);
        OrderDetailHibernateDAO orderDetailHibernateDAO = this.hibernateUtil.getOrderDetailHibernateDAO();
        List<OrderDetail> orderDetailList = orderDetailHibernateDAO.getAll(page,size,status);
        Long totalCount = orderDetailHibernateDAO.countWithStatus(status);
        int count = orderDetailList.size();
        List<OrderDetailResponse> orderDetailResponses = orderDetailList.stream().map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class)).collect(Collectors.toList());

        AllOrderDetailsResponse allOrderDetailsResponse = new AllOrderDetailsResponse();
        allOrderDetailsResponse.setOrderDetails( orderDetailResponses );
        allOrderDetailsResponse.setCount(count );
        allOrderDetailsResponse.setTotalCount(totalCount );
        allOrderDetailsResponse.setLinks(preparePaginationLinks(totalCount, size, page, requestURL ) );


        return allOrderDetailsResponse;
    }
}


