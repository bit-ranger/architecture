package top.rainynight.site.blog.ws;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import top.rainynight.site.blog.entity.Article;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@WebService
@Path("/")
public interface BlogWS {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Article> list();

    @GET
    @Path(value = "{id:[1-9]{1,9}}")
    @Produces(MediaType.APPLICATION_JSON)
    Article get(@PathParam("id") int id);

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    boolean post(@Multipart("title") String title, @Multipart("content") String content, @Multipart("classId") int classId);

}
