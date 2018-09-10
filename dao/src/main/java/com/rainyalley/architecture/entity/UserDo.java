package com.rainyalley.architecture.entity;

import com.rainyalley.architecture.AbstractJsonObject;
import com.rainyalley.architecture.Identical;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

public class UserDo extends AbstractJsonObject implements Serializable,Identical {

    private static final long serialVersionUID = 2894257361469960272L;

    @NotBlank
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String password;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDo)) return false;

        UserDo userDo = (UserDo) o;

        if (getId() != null ? !getId().equals(userDo.getId()) : userDo.getId() != null) return false;
        if (getName() != null ? !getName().equals(userDo.getName()) : userDo.getName() != null) return false;
        return getPassword() != null ? getPassword().equals(userDo.getPassword()) : userDo.getPassword() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }
}
