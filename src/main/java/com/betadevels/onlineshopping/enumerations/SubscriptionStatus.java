package com.betadevels.onlineshopping.enumerations;


import lombok.Getter;

public enum SubscriptionStatus
{
	ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

	@Getter
	private final String status;
	SubscriptionStatus( String status )
	{
		this.status = status;
	}
}
