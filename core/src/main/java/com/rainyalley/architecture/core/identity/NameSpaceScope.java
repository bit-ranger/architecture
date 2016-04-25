package com.rainyalley.architecture.core.identity;

public class NameSpaceScope implements Scope {

    private String nameSpace;

    public NameSpaceScope(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    @Override
    public String toString() {
        return nameSpace;
    }
}
