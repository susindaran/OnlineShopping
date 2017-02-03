package com.utdallas.onlineshopping.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collections;

/**
 * Created by susindaran on 2/3/17.
 */
public class AuthorizationFailedException extends WebApplicationException
{
    public AuthorizationFailedException()
    {
        super(Response.status(Response.Status.UNAUTHORIZED).build());
    }

    public AuthorizationFailedException(Object object)
    {
        super(Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("error", object)).build());
    }
}
