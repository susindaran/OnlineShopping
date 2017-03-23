package com.utdallas.onlineshopping.payload.request.orderDetail;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Getter;

import java.util.List;

/**
 * Created by vidya on 3/22/17.
 */

@JsonSnakeCase
@Getter
public class GetOrderDetailRequest {

        private List<Integer> orderDetailId;
        private String order_detail_status;
        private org.joda.time.LocalDateTime updatedAt;

}
