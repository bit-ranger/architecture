package com.rainyalley.architecture.util;

public class AtomicRunnerAdapter implements AtomicRunner {
    @Override
    public boolean runnable() {
        return true;
    }

    @Override
    public boolean run() {
        return true;
    }

    @Override
    public boolean commit() {
        return true;
    }

    @Override
    public boolean rollback() {
        return true;
    }
}
