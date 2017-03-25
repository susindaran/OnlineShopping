package com.betadevels.onlineshopping.configurations;

import io.dropwizard.db.DataSourceFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class OnlineShoppingConfiguration extends Configuration
{
    @NotNull
    @JsonProperty("endpoint")
    private String endpoint;

    @Valid
    @NotNull
    @JsonProperty("databaseConfiguration")
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    public DataSourceFactory getDatabaseConfiguration()
    {
        return dataSourceFactory;
    }

    @JsonProperty("staticContentRoot")
    @Getter
	private String staticContentRoot;
}
