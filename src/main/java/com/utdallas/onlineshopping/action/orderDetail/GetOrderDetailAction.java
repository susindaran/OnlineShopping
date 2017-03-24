package com.utdallas.onlineshopping.action.orderDetail;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.Action;
import com.utdallas.onlineshopping.db.hibernate.OrderDetailHibernateDAO;
import com.utdallas.onlineshopping.models.OrderDetail;
import com.utdallas.onlineshopping.payload.response.orderdetail.GetAllOrderDetailsResponse;
import com.utdallas.onlineshopping.payload.response.orderdetail.OrderDetailResponse;
import com.utdallas.onlineshopping.util.HibernateUtil;
import com.utdallas.onlineshopping.validators.orderDetail.OrderDetailValidator;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by vidya on 3/22/17.
 */
public class GetOrderDetailAction implements Action<GetAllOrderDetailsResponse>
    {
        private HibernateUtil hibernateUtil;
        private ModelMapper modelMapper;
        private String requestURL;
        private int page, size;
        private String status;

    @Inject
    public GetOrderDetailAction(Provider<HibernateUtil> hibernateUtilProvider, ModelMapper modelMapper)
        {
            this.hibernateUtil = hibernateUtilProvider.get();
            this.modelMapper = modelMapper;
        }

    public GetOrderDetailAction withRequestURL(String requestURL)
    {
        this.requestURL = requestURL;
        return this;
    }

    public GetOrderDetailAction withPaginateDetails(int page, int size, String status)
    {
        this.page = page;
        this.size = size;
        this.status=status;
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
    public GetAllOrderDetailsResponse invoke()
    {
        OrderDetailValidator.validateQueryParams(page, size);
        OrderDetailHibernateDAO orderDetailHibernateDAO = this.hibernateUtil.getOrderDetailHibernateDAO();
        List<OrderDetail> orderDetailList = orderDetailHibernateDAO.getAll(page,size,status);
        Long totalCount = orderDetailHibernateDAO.count();
        int count = orderDetailList.size();
        int flag=0;
        List<OrderDetailResponse> orderDetailResponses = orderDetailList.stream().map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class)).collect(Collectors.toList());

        GetAllOrderDetailsResponse getAllOrderDetailsResponse = new GetAllOrderDetailsResponse();
        getAllOrderDetailsResponse.setOrderDetailsResponses( orderDetailResponses );
        getAllOrderDetailsResponse.setCount(count);
        getAllOrderDetailsResponse.setTotalCount(totalCount);
        getAllOrderDetailsResponse.setLinks(preparePaginationLinks(totalCount));


        return getAllOrderDetailsResponse;
    }
}


