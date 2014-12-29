package org.sllx.storage;

import javax.jws.WebService;

@WebService
public interface StorageService {

    Archive download(String id);

    String upload(Archive archive);
}
