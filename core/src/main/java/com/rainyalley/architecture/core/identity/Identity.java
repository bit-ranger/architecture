package com.rainyalley.architecture.core.identity;

public class Identity implements Identifier {

    private final static Scope DEFAULT_SCOPE = new DefaultScope();

    private Scope scope;

    private String id;


    public Identity(Scope scope, String id){
        this.scope = scope;
        this.id = id;
    }

    public Identity(String id){
        this(DEFAULT_SCOPE, id);
    }

    @Override
    public Scope scope() {
        return scope;
    }

    @Override
    public String getId() {
        return id;
    }
}
