import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.sllx.storage.StorageService;
import org.sllx.storage.StorageServiceImpl;

/**
 * Created by sllx on 14-12-24.
 */
public class Test {
    public static void main(String[] args){
        DynamicClientFactory factory = DynamicClientFactory.newInstance();
        Client client = factory.createClient("http://localhost:8081/storage?wsdl");

        try {
            Object[] results = client.invoke("download");
            System.out.println(results[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
