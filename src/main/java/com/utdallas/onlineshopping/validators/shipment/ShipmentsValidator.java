package com.utdallas.onlineshopping.validators.shipment;

import com.utdallas.onlineshopping.exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prathyusha on 3/18/17.
 */
public class ShipmentsValidator
{
    public static void validateQueryParams(int page, int size)
    {
        List<String> errors = new ArrayList<>();
        if( page < 1 )
        {
            errors.add( "page parameter must be at least 1");
        }
        if( size < 1)
        {
            errors.add( "size parameter must be at least 1");
        }

        if( errors.size() > 0 )
        {
            throw new BadRequestException(errors);
        }
    }
}
