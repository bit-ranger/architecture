package org.sllx.site.core;


public enum Relation {
    LIST("list"),
    GET("get"),
    POST("post"),
    DELETE("delete"),
    PUT("put");

    private String name;

    private Relation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
