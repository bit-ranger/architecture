package com.rainyalley.architecture.graphql;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * @author bin.zhang
 */
public interface SecurityContext {

    /**
     * 获取用户Id
     * @return
     */
    @NotNull
    SecurityUser getUser(HttpServletRequest httpServletRequest);


}
