package com.rainyalley.architecture.service;

import com.rainyalley.architecture.core.identity.ClassPathScope;
import com.rainyalley.architecture.core.identity.Identity;
import com.rainyalley.architecture.core.identity.Scope;

public abstract class AbstractEntity extends Identity {

    private final Scope scope = new ClassPathScope(getClass());

    private String id;

    public AbstractEntity() {
        super(null, null);
    }

    @Override
    public Scope scope() {
        return this.scope;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
