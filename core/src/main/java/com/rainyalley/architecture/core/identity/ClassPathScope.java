package com.rainyalley.architecture.core.identity;

public class ClassPathScope implements Scope {

    private Class<?> clazz;

    public ClassPathScope(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return clazz.getClass().getName();
    }
}
