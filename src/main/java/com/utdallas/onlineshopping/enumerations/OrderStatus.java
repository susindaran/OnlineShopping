package com.utdallas.onlineshopping.enumerations;

import lombok.Getter;

public enum OrderStatus
{
    PENDING("PENDING"), SHIPPED("SHIPPED"), INVOICED("INVOICED");

    @Getter
    private String status;
    OrderStatus(String status)
    {
        this.status = status;
    }
}
