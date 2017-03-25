package com.betadevels.onlineshopping.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collections;

public class NotFoundException extends WebApplicationException
{
    public NotFoundException()
    {
        super(Response.status(Response.Status.NOT_FOUND).build());
    }

    public NotFoundException(Object object)
    {
        super(Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("errors", object)).build());
    }
}
