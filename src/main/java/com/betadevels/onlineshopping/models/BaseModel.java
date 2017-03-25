package com.betadevels.onlineshopping.models;

import org.joda.time.DateTimeZone;

import java.io.Serializable;

public abstract class BaseModel implements Serializable
{
    private static final DateTimeZone timeZone = DateTimeZone.forID("America/Chicago");
}
