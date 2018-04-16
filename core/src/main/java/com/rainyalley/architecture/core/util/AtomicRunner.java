package com.rainyalley.architecture.core.util;

public interface AtomicRunner {

    boolean run();

    void commit();

    void rollback();
}
