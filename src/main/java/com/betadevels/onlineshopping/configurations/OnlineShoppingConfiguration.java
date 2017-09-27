package com.betadevels.onlineshopping.configurations;

import io.dropwizard.db.DataSourceFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;

public class OnlineShoppingConfiguration extends Configuration
{
    @NotNull
    @JsonProperty("endpoint")
    private String endpoint;

    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory = getDS();

    public OnlineShoppingConfiguration() throws URISyntaxException
    {
    }

    public DataSourceFactory getDatabaseConfiguration()
    {
        return dataSourceFactory;
    }

    @JsonProperty("staticContentRoot")
    @Getter
	private String staticContentRoot;

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    public static DataSourceFactory getDS() throws URISyntaxException
    {
        URI jdbUri = new URI(System.getenv("JAWSDB_URL" ));

        String username = jdbUri.getUserInfo().split(":")[0];
        String password = jdbUri.getUserInfo().split(":")[1];
        String port = String.valueOf(jdbUri.getPort());
        String jdbUrl = "jdbc:mysql://" + jdbUri.getHost() + ":" + port + jdbUri.getPath() + "?useSSL=false";

        DataSourceFactory fac = new DataSourceFactory();
        fac.setUrl( jdbUrl );
        fac.setUser( username );
        fac.setPassword( password );
        fac.setDriverClass( "com.mysql.jdbc.Driver" );
        fac.setMinSize( 3 );
        fac.setInitialSize( 3 );
        fac.setMaxSize( 8 );

        System.out.println(jdbUrl);
        System.out.println(username);
        System.out.println(password);

        return fac;
    }
}
