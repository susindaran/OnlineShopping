package com.betadevels.onlineshopping.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collections;

public class ConflictingRequestException extends WebApplicationException
{
    public ConflictingRequestException()
    {
        super(Response.status(Response.Status.CONFLICT).build());
    }

    public ConflictingRequestException(Object object)
    {
        super(Response.status(Response.Status.CONFLICT).entity(Collections.singletonMap("error", object)).build());
    }
}
