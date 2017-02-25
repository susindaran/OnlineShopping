package com.utdallas.onlineshopping.util;

import org.joda.time.LocalDateTime;

public class Utility
{
    public static LocalDateTime StringToDate( String date )
    {
        String[] parts = date.split("/");
        return LocalDateTime.now().withMonthOfYear( Integer.parseInt( parts[0] ) ).withYear( Integer.parseInt( parts[1] ) );
    }
}
