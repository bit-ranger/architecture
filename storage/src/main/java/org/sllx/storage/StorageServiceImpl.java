package org.sllx.storage;

import javax.jws.WebService;

/**
 * Created by sllx on 14-12-27.
 */
@WebService
public class StorageServiceImpl implements StorageService{

    @Override
    public Archive download() {
        return new Archive();
    }
}
