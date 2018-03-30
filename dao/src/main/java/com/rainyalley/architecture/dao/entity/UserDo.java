package com.rainyalley.architecture.dao.entity;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class UserDo implements Serializable {

    private static final long serialVersionUID = 2894257361469960272L;

    private Integer id;
    private String name;
    private String password;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotBlank
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        UserDo user = (UserDo) o;

        if (this.id != user.id) return false;
        if (this.name != null ? !this.name.equals(user.name) : user.name != null) return false;
        return this.password != null ? this.password.equals(user.password) : user.password == null;

    }

    @Override
    public int hashCode() {
        int result = this.id != null ? this.id : 0;
        result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
        result = 31 * result + (this.password != null ? this.password.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
