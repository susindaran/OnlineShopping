package com.utdallas.onlineshopping.payload.response.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utdallas.onlineshopping.models.Address;
import com.utdallas.onlineshopping.models.Customer;
import com.utdallas.onlineshopping.models.Product;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;
import org.joda.time.LocalDateTime;

import java.util.List;

@JsonSnakeCase
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse
{
    private int orderId;
    private Customer customer;
    private String orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Address shippingAddress;
    private Address billingAddress;
    private List<ShipmentResponse> shipments;

    @JsonSnakeCase
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ShipmentResponse
    {
        private int shipmentId;
        private LocalDateTime deliveryDueDate;
        private String shipmentStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<OrderDetailResponse> orderDetails;

        @JsonSnakeCase
        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private static class OrderDetailResponse
        {
            private int orderDetailId;
            private Product product;
            private int quantity;
        }
    }
}
