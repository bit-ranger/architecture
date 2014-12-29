package org.sllx.storage;

import javax.jws.WebService;

@WebService
public class StorageServiceImpl implements StorageService{

    @Override
    public Archive download(String id) {
        return new Archive();
    }

    @Override
    public String upload(Archive archive) {
        return null;
    }
}
