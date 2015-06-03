package top.rainynight.site.storage.ws;


import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@WebService
@Path("/")
public interface StorageWS {

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA,MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.TEXT_PLAIN)
    String upload(@Multipart("file") Attachment file);

    @GET
    @Path("{id:[1-9]{1,9}}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    byte[] download(@PathParam("id") String id);
}
