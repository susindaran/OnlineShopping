package com.utdallas.onlineshopping.validators.orderDetail;

import com.utdallas.onlineshopping.exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vidya on 3/22/17.
 */
public class OrderDetailValidator {
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

