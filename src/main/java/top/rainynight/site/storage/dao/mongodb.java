package top.rainynight.site.storage.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.lang.String;import java.lang.System;import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by sllx on 9/19/15.
 */
public class mongodb {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("documents");

        // insert a document
//        IntStream.rangeClosed(1,5).forEach(i -> {
//            Document document = new Document("x", i);
//            collection.insertOne(document);
//        });
//
//
//        // replace a document
//        Document document = new Document("$set", new Document("y",998));
//        collection.updateMany(Filters.eq("x", 3), document);
//
//        // find documents
//        List<Document> foundDocument = collection.find().into(new ArrayList<Document>());
//        foundDocument.forEach(doc -> System.out.println(doc));
//
//        collection.deleteMany(new Document());

        client.close();
    }

}
