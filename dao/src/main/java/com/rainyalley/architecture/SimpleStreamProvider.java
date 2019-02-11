package com.rainyalley.architecture;

import lombok.extern.slf4j.Slf4j;
import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import java.io.Closeable;

/**
 * @author bin.zhang
 */
@Validated
@Slf4j
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

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public <E> void persist(E entity) {
        entityManager.persist(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public <E> void remove(E entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    @Override
    public void close() {
        entityManager.close();
    }
}
