package org.sllx.storage;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;

@WebService
public interface StorageService {

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    List<Archive> retrieveAllProducts();

    @GET
    @Path("/product/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Archive retrieveProductById(@PathParam("id") long id);

    @POST
    @Path("/products")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    List<Archive> retrieveProductsByName(@FormParam("name") String name);

    @POST
    @Path("/product")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Archive createProduct(Archive archive);

    /**
     * 在WebService中参数类型不能为接口
     * @param id
     * @param fieldMap
     * @return
     */
    @PUT
    @Path("/product/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Archive updateProductById(@PathParam("id") long id, HashMap<String, Object> fieldMap);

    @DELETE
    @Path("/product/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Archive deleteProductById(@PathParam("id") long id);
}
