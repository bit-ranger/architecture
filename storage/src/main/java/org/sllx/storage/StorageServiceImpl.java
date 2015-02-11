package org.sllx.storage;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.jws.WebService;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StorageServiceImpl implements StorageService{


    @Override
    public List<Archive> retrieveAllProducts() {

        List<Archive> list = new ArrayList<Archive>();

        list.add(new Archive("ArchiveA"));
        list.add(new Archive("ArchiveB"));
        list.add(new Archive("ArchiveC"));

        return list;
    }

    @Override
    public Archive retrieveProductById(long id) {
        return null;
    }

    @Override
    public String createProduct(String name, Attachment image) {
        try {
            image.transferTo(new File("E:/tmp.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Archive updateProductById(long id, HashMap<String, Object> fieldMap) {
        return null;
    }

    @Override
    public Archive deleteProductById(long id) {
        return null;
    }
}
