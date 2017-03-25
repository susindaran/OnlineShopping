package com.betadevels.onlineshopping.health;

import com.codahale.metrics.health.HealthCheck;

public class OnlineShoppingHealthCheck extends HealthCheck
{
    protected Result check() throws Exception
    {
        return Result.healthy();
    }
}
