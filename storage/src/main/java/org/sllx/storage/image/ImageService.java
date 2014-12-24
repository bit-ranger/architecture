package org.sllx.storage.image;

import org.sllx.storage.StorageService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path(value = "image")
public interface ImageService extends StorageService{

    @GET
    @Path(value = "/{id}")
    String download(@PathParam("id") String id);

}
