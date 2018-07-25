package com.rainyalley.architecture.filter.limit;

/**
 * @author bin.zhang
 */
public interface Console {

    CallerLimit getCallerLimit(String caller);

    CallerRuntime getCallerRuntime(String caller);

    TargetLimit getTargetLimit(String target);

    TargetRuntime getTargetRuntime(String target);

    boolean acquireConcurrency(String caller, String target);

    boolean releaseConcurrency(String caller, String target);

    boolean hasAuth(String caller, String target);

    boolean access(String caller, String target);
}
