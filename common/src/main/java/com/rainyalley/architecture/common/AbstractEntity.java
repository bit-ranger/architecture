package com.rainyalley.architecture.common;

import com.rainyalley.architecture.core.identity.ClassPathScope;
import com.rainyalley.architecture.core.identity.Identity;
import com.rainyalley.architecture.core.identity.Scope;

public abstract class AbstractEntity extends Identity {

    private Scope scope = new ClassPathScope(this.getClass());

    private String id;

    public AbstractEntity() {
        super(null, null);
    }

    @Override
    public Scope scope() {
        return scope;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
