package com.rainyalley.architecture.filter.limit;

import javax.servlet.http.HttpServletRequest;

public interface LimitStrategy {

    /**
     *
     * @return 全局吞吐量控制
     */
    Limit getGlobalLimit();

    /**
     *
     * @return 指定用户全局吞吐量控制
     */
    Limit getCallerLimit(String caller);

    /**
     *
     * @return 指定接口吞吐量控制
     */
    Limit getTargetLimit(String target);

    /**
     *
     * @return 指定用户与接口吞吐量控制
     */
    Limit getTargetCallerLimit(String target, String caller);


    /**
     * @param request
     * @return 是否有效的调用
     */
    boolean isValidCall(HttpServletRequest request);

    /**
     *
     * @return 有调用权限
     */
    boolean hasAuth(String target, String caller);
}
