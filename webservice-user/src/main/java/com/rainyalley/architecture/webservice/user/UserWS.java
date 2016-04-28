package com.rainyalley.architecture.webservice.user;


import com.rainyalley.architecture.common.user.model.entity.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
public interface UserWS {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<User> browse();

    @GET
    @Path("{id:[1-9]{1,9}}")
    @Produces(MediaType.APPLICATION_JSON)
    User lookOver(@PathParam("id") int id);
}
