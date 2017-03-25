package com.betadevels.onlineshopping.util;

import org.joda.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

public class Utility
{
    public static LocalDateTime StringToDate( String date )
    {
        String[] parts = date.split("/");
        return LocalDateTime.now().withMonthOfYear( Integer.parseInt( parts[0] ) ).withYear( Integer.parseInt( parts[1] ) );
    }

	public static String createLink(int page, int size, String requestURL)
	{
		return requestURL + "?page=" + page + "&size=" + size;
	}

    public static Map<String, String> preparePaginationLinks(Long totalCount, int size, int page, String requestURL)
    {
	    Map<String, String> linksMap = new HashMap<>();
	    int pages = Math.toIntExact((totalCount / size) + (totalCount % size == 0 ? 0 : 1));
	    linksMap.put("first", createLink(1, size, requestURL));
	    linksMap.put("prev", page > 1 ? createLink(page - 1, size, requestURL) : null);
	    linksMap.put("next", page * size < totalCount ? createLink( page + 1, size, requestURL ) : null);
	    linksMap.put("last", createLink(pages, size, requestURL));

	    return linksMap;
    }
}
