package org.sllx.site.blog.entity;

/**
 * Created by sllx on 14-11-27.
 */
public class Homepage {
    private Integer userid;
    private String keyword;
    private String description;
    private String name;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        Homepage homepage = (Homepage) o;

        if (userid != homepage.userid) return false;
        if (description != null ? !description.equals(homepage.description) : homepage.description != null)
            return false;
        if (keyword != null ? !keyword.equals(homepage.keyword) : homepage.keyword != null) return false;
        if (name != null ? !name.equals(homepage.name) : homepage.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userid;
        result = 31 * result + (keyword != null ? keyword.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
