package com.utdallas.onlineshopping.payload.request.orderDetail;

import com.utdallas.onlineshopping.enumerations.OrderStatus;
import com.utdallas.onlineshopping.models.OrderDetail;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import java.util.List;

/**
 * Created by vidya on 3/22/17.
 */

@JsonSnakeCase
@Getter
public class GetOrderDetailRequest {

        private List<Long> orderDetailId;
        private OrderStatus order_detail_status;
        private org.joda.time.LocalDateTime updatedAt;

}
