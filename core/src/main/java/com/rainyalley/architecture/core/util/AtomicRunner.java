package com.rainyalley.architecture.core.util;

public interface AtomicRunner {

    boolean runnable();

    boolean run();

    boolean commit();

    boolean rollback();
}
