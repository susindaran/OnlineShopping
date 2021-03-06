package com.betadevels.onlineshopping.validators;

import com.betadevels.onlineshopping.exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;

public class GenericValidator
{
	public static void validatePaginateParameters(int page, int size)
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
		else if( size > 50 )
		{
			errors.add( "size parameter must be at most 50" );
		}

		if( errors.size() > 0 )
		{
			throw new BadRequestException(errors);
		}
	}
}
