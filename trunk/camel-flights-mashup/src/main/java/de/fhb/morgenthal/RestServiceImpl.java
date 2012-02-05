package de.fhb.morgenthal;

import javax.ws.rs.*;

import de.fhb.morgenthal.vo.Flight;

@Path("/rss-feed/")
public class RestServiceImpl {

    public RestServiceImpl() {
    }

    @GET
    @Path("/DE")
    public String getRssD() {
        return null;
    }

    @GET
    @Path("/AT")
    public String getRssA() {
        return null;
    }

    @GET
    @Path("/CH")
    public String getRssC() {
        return null;
    }
}