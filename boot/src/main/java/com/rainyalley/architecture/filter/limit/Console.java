package com.rainyalley.architecture.filter.limit;

import java.util.List;

/**
 * @author bin.zhang
 */
public interface Console {

    List<String> getCallerAuth(String caller);

    boolean hasAuth(String caller, String target);

    long getCallerAccessCount(String caller);

    List<Access> getCallerAccessList(String caller, long start, long end);

    CallerLimit getCallerLimit(String caller, String target);

    CallerRuntime getCallerRuntime(String caller, String target);

    TargetLimit getTargetLimit(String target);

    TargetRuntime getTargetRuntime(String target);

    boolean acquireConcurrency(String caller, String target);

    boolean releaseConcurrency(String caller, String target);

    boolean access(String caller, String target);
}
