package top.rainynight.site.user.ws;


import top.rainynight.site.user.entity.User;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@WebService
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
