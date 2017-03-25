package com.betadevels.onlineshopping.enumerations;

import lombok.Getter;

public enum ShipmentStatus
{
    PICKED("PICKED"), PACKED("PACKED"), SHIPPED("SHIPPED");

    @Getter
    private final String status;
    ShipmentStatus(String status)
    {
        this.status = status;
    }
}
