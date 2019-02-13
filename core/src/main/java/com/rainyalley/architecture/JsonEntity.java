package com.rainyalley.architecture;

public abstract class JsonEntity {

    public String toJson() {
        return "";
    }

    public Object toObject(String json) {
        return null;
    }

    @Override
    public String toString() {
        return toJson();
    }
}
