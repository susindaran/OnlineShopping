package com.utdallas.onlineshopping.enumerations;

import lombok.Getter;

public enum OrderStatus
{
    PENDING("PENDING"), SHIPPED("SHIPPED"), INVOICED("INVOICED"), RETURN_INITIATED("RETURN_INITIATED"), RETURN_RECEIVED("RETURN_RECEIVED");

    @Getter
    private final String status;
    OrderStatus(String status)
    {
        this.status = status;
    }
}
