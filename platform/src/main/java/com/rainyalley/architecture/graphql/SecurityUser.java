package com.rainyalley.architecture.graphql;

import java.util.Collections;
import java.util.List;

/**
 * @author bin.zhang
 */
public class SecurityUser {

    private boolean isAuthenticated;

    private String userId = "anonymous";

    private List<String> authorities = Collections.emptyList();

    public SecurityUser() {
        this.isAuthenticated = false;
    }


    public SecurityUser(String userId) {
        this.userId = userId;
        isAuthenticated = true;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityUser)){
            return false;
        }

        SecurityUser that = (SecurityUser) o;

        if (isAuthenticated() != that.isAuthenticated()){
            return false;
        }
        return getUserId() != null ? getUserId().equals(that.getUserId()) : that.getUserId() == null;
    }

    @Override
    public int hashCode() {
        int result = (isAuthenticated() ? 1 : 0);
        result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
        return result;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
