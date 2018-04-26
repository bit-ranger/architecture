package com.rainyalley.architecture.util;

public interface AtomicRunner {

    boolean runnable();

    boolean run();

    boolean commit();

    boolean rollback();
}
