package org.sllx.site.blog.entity;

import java.io.File;
import java.io.Serializable;

public class Archive implements Serializable{

    private static final long serialVersionUID = -6265714340584955008L;
    private String name;
    private File body;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getBody() {
        return body;
    }

    public void setBody(File archive) {
        this.body = archive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Archive other = (Archive) o;

        if (body != null ? !body.equals(other.body) : other.body != null ) return false;
        if (name != null ? !name.equals(other.name) : other.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
