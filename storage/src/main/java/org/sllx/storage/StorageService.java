package org.sllx.storage;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;

@Path("/")
public interface StorageService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Archive> retrieveAllProducts();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Archive retrieveProductById(@PathParam("id") long id);


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    String createProduct(@Multipart(value="name")String name,
                          @Multipart(value="body")Attachment image);

    /**
     * 在WebService中参数类型不能为接口
     * @param id
     * @param fieldMap
     * @return
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Archive updateProductById(@PathParam("id") long id, HashMap<String, Object> fieldMap);

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Archive deleteProductById(@PathParam("id") long id);
}
