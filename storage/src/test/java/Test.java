//import org.apache.cxf.endpoint.Client;
//import org.apache.cxf.endpoint.ClientImpl;
//import org.apache.cxf.endpoint.Endpoint;
//import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
//import org.apache.cxf.service.model.*;
//import org.sllx.storage.Archive;
//
//import javax.activation.DataHandler;
//import javax.activation.FileDataSource;
//import javax.xml.namespace.QName;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Method;
//import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception{

//        DynamicClientFactory factory = DynamicClientFactory.newInstance();
//        Client client = factory.createClient("http://localhost:8081/storage?wsdl");
//
//        DataHandler dh = new DataHandler(new FileDataSource(new java.io.File("/home/sllx/test.txt")));
//
//        ClientImpl clientImpl = (ClientImpl) client;
//        Endpoint endpoint = clientImpl.getEndpoint();
//        // Make use of CXF service model to introspect the existing WSDL
//        ServiceInfo serviceInfo = endpoint.getService().getServiceInfos().get(0);
//        // 创建QName来指定NameSpace和要调用的service
//        QName bindingName = new QName("http://storage.sllx.org/","StorageServiceImplServiceSoapBinding");
//        BindingInfo binding = serviceInfo.getBinding(bindingName);
//        // 创建QName来指定NameSpace和要调用的方法
//        QName opName = new QName("http://storage.sllx.org/","upload");
//
//        BindingOperationInfo boi = binding.getOperation(opName);
//        BindingMessageInfo inputMessageInfo = boi.getInput();
//        List<MessagePartInfo> parts = inputMessageInfo.getMessageParts();
//        // 取得对象实例
//        MessagePartInfo partInfo = parts.get(0);
//        Class<?> partClass = partInfo.getTypeClass();
//        Object inputObject = partClass.newInstance();
//
//        // 取得字段的set方法并赋值
//        PropertyDescriptor partPropertyDescriptor = new PropertyDescriptor("name", partClass);
//        Method userNoSetter = partPropertyDescriptor.getWriteMethod();
//        userNoSetter.invoke(inputObject, "test");
//
//        // 取得字段的set方法并赋值
//        PropertyDescriptor partPropertyDescriptor2 = new PropertyDescriptor("body", partClass);
//        Method productCodeSetter = partPropertyDescriptor2.getWriteMethod();
//        productCodeSetter.invoke(inputObject, dh);
//
//        try {
//            Object[] results = client.invoke("upload", inputObject);
//            System.out.println(results[0]);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
