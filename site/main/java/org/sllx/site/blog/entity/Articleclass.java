package org.sllx.site.blog.entity;

/**
 * Created by sllx on 14-11-27.
 */
public class Articleclass {
    private int classid;
    private int userid;
    private String name;

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Articleclass that = (Articleclass) o;

        if (classid != that.classid) return false;
        if (userid != that.userid) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = classid;
        result = 31 * result + userid;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
