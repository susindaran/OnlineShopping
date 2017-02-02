package com.utdallas.onlineshopping.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collections;

public class BadRequestException extends WebApplicationException
{
    public BadRequestException()
    {
        super(Response.status(Response.Status.BAD_REQUEST).build());
    }

    public BadRequestException(Object object)
    {
        super(Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("error", object)).build());
    }
}
