package com.rainyalley.architecture.core.identity;

public class Identity implements Identifier {

    private static final Scope DEFAULT_SCOPE = new DefaultScope();

    private final Scope scope;

    private final String id;


    public Identity(Scope scope, String id) {
        this.scope = scope;
        this.id = id;
    }

    public Identity(String id) {
        this(Identity.DEFAULT_SCOPE, id);
    }

    @Override
    public Scope scope() {
        return this.scope;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
