package com.betadevels.onlineshopping.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collections;

public class InternalErrorException extends WebApplicationException
{
    public InternalErrorException()
    {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }

    public InternalErrorException(Object object)
    {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("error", object)).build());
    }
}
