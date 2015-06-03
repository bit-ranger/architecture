package top.rainynight.site.storage.entity;

import java.io.Serializable;
import java.util.Arrays;

public class Storage implements Serializable {

    private static final long serialVersionUID = -6265714340584955008L;
    private String name;
    private  byte[] body;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  byte[] getBody() {
        return body;
    }

    public void setBody( byte[] archive) {
        this.body = archive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Storage other = (Storage) o;

        if (body != null ? !Arrays.equals(body, other.body) : other.body != null ) return false;
        if (name != null ? !name.equals(other.name) : other.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (body != null ? Arrays.hashCode(body) : 0);
        return result;
    }
}
