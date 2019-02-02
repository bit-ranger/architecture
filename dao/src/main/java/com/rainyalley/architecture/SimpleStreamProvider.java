package com.rainyalley.architecture;

import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;

import javax.persistence.EntityManager;
import java.io.Closeable;

/**
 * @author bin.zhang
 */
public class SimpleStreamProvider implements StreamProvider, Closeable {

    private JinqJPAStreamProvider jinqJPAStreamProvider;

    private EntityManager entityManager;


    public SimpleStreamProvider(JinqJPAStreamProvider jinqJPAStreamProvider, EntityManager entityManager) {
        this.jinqJPAStreamProvider = jinqJPAStreamProvider;
        this.entityManager = entityManager;
    }

    @Override
    public <U> JPAJinqStream<U> stream(Class<U> clazz){
        return jinqJPAStreamProvider.streamAll(entityManager, clazz);
    }

    @Override
    public void close() {
        entityManager.close();
    }
}
