package top.rainynight.site.storage.ws;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.stereotype.Component;
import top.rainynight.site.storage.entity.Storage;
import top.rainynight.site.storage.service.StorageService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;


@Component("storageWS")
public class StorageWSImpl implements StorageWS{

    @javax.annotation.Resource(name = "storageService")
    private StorageService storageService;

    @Override
    public String upload(Attachment file) {
        String id = UUID.randomUUID().toString();
        Storage storage = new Storage();
        storage.setName(id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            file.getDataHandler().writeTo(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        storage.setBody(baos.toByteArray());
        storageService.save(storage);
        return id;
    }

    @Override
    public byte[] download(String id) {
        Storage storage = new Storage();
        storage.setName(id);
        return storageService.get(storage).getBody();
    }
}
