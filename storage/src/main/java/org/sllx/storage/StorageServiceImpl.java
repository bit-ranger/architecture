package org.sllx.storage;

import javax.jws.WebService;
import javax.ws.rs.FormParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebService
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
    public List<Archive> retrieveProductsByName(@FormParam("name") String name) {
        return null;
    }

    @Override
    public Archive createProduct(Archive archive) {
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
