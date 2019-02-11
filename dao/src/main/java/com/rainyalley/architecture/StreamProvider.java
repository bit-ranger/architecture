package com.rainyalley.architecture;

import org.jinq.jpa.JPAJinqStream;

/**
 * @author bin.zhang
 */
public interface StreamProvider {

    <U> JPAJinqStream<U> stream(Class<U> clazz);

    <E> void persist(E entity);

}
