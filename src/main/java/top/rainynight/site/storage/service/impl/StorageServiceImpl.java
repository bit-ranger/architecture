package top.rainynight.site.storage.service.impl;


import org.springframework.stereotype.Service;
import top.rainynight.core.ServiceBasicSupport;
import top.rainynight.site.storage.dao.StorageDao;
import top.rainynight.site.storage.entity.Storage;
import top.rainynight.site.storage.service.StorageService;

@Service("storageService")
public class StorageServiceImpl extends ServiceBasicSupport<Storage> implements StorageService {
    private StorageDao storageDao;

    public void setStorageDao(StorageDao storageDao) {
        setDao(storageDao);
        this.storageDao = storageDao;
    }
}
