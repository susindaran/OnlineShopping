package com.betadevels.onlineshopping.enumerations;

import lombok.Getter;

public enum TransactionType
{
    CREDIT("CREDIT"), DEBIT("DEBIT");

    @Getter
    private final String type;
    TransactionType(String type)
    {
        this.type = type;
    }
}
