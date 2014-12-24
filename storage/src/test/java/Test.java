import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;
import java.io.File;

/**
 * Created by sllx on 14-12-24.
 */
public class Test {
    public static void main(String[] args){
        String target = "http://localhost:8081/image";
        WebClient client = WebClient.create(target);
        String result = client.path("/1").accept(MediaType.APPLICATION_JSON).get(String.class);
        //Object result = client.path("/").accept(MediaType.MULTIPART_FORM_DATA_TYPE).post("abc");
        System.out.println(result);
    }
}
