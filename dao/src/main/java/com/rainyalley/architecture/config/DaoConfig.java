package com.rainyalley.architecture.config;

import com.rainyalley.architecture.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.rainyalley.architecture.repository")
@Configuration
public class DaoConfig {


//    @Bean
    public CommandLineRunner initData(MongoOperations mongo) {
        return (String... args) -> {
            mongo.dropCollection(User.class);
            mongo.createCollection(User.class, CollectionOptions.empty().maxDocuments(200).size(100000).capped());
        };
    }
}
