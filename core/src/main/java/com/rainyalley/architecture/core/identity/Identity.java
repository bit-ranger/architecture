package com.rainyalley.architecture.core.identity;

public class Identity implements Identifier {

    private Scope scope;

    private String id;

    public Identity(Scope scope, String id){
        this.scope = scope;
        this.id = id;
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public String getId() {
        return id;
    }
}
