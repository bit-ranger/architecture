package org.sllx.storage.image;

import org.sllx.storage.Archive;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path(value = "/")
public class ImageServiceImpl{

    @GET
    @Path(value = "{id}")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Archive download(@PathParam("id") String id) {
        Archive archive = new Archive();
        archive.setName(id);
        return archive;
    }

    @POST
    public String upload(){
        return "success";
    }
}
